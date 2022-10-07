package com.squirtle.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.squirtle.R;
import com.squirtle.activities.MainActivity;
import com.squirtle.activities.ModulesActivity;
import com.squirtle.databinding.ActivityMainBinding;
import com.squirtle.databinding.FragmentLoginBinding;
import com.squirtle.model.Usuario;
import com.squirtle.model.UsuarioLogado;
import com.squirtle.utils.JWTUtils;
import com.squirtle.utils.LogoutUtils;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private String jwtLogin;
    private UsuarioLogado usuarioLogado;

    private TextInputLayout emailLayout;
    private TextInputLayout senhaLayout;
    private EditText emailField;
    private EditText senhaField;
    private Button buttonLogin;
    private TextView tryingText;
    private ProgressBar progressBar;


    private ConstraintLayout othersLayout;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        buttonLogin = view.findViewById(R.id.buttonLogin);
        Button signup_button = view.findViewById(R.id.signup_button);
        Button esqueci_minha_senha_button = view.findViewById(R.id.esqueci_minha_senha_button);

        emailField = view.findViewById(R.id.emailText);
        senhaField = view.findViewById(R.id.senhaText);
        emailLayout = view.findViewById(R.id.emailLayout);
        senhaLayout = view.findViewById(R.id.senhaLayout);

        emailField.setText("teste@teste.com");
        senhaField.setText("123");
        progressBar = view.findViewById(R.id.progressBar);
        tryingText = view.findViewById(R.id.tentando_login_text);

        othersLayout = view.findViewById(R.id.signup_layout);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_SignupFragment);
            }
        });

        esqueci_minha_senha_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Deslogando...", Toast.LENGTH_SHORT).show();
                LogoutUtils.clearShared(getContext());
//                NavHostFragment.findNavController(LoginFragment.this)
//                        .navigate(R.id.action_LoginFragment_to_ForgotFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void login(){
        try {
            HttpAsync httpAsync = new HttpAsync(new URL(
                    getString(R.string.squirtle_api) + getString(R.string.squirtle_api_endpoint) +
                            getString(R.string.squirtle_api_endpoint_login)));
            httpAsync.setDebug(true);
            httpAsync.addParam("email", emailField.getText());
            httpAsync.addParam("senha", senhaField.getText());
            httpAsync.post(new FutureCallback() {
                @Override
                public void onBeforeExecute() {
                    disableForm();
                }

                @Override
                public void onAfterExecute() {
                    if(usuarioLogado == null){
                        Toast.makeText(getContext(), "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
                        emailField.setError("Verifique seu email!");
                        senhaField.setError("Verifique sua senha!");
                    }else{
                        Toast.makeText(getContext(), "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), ModulesActivity.class);
                        intent.putExtra("usuarioLogado", usuarioLogado);
                        startActivity(intent);
                    }
                    enableForm();
                }

                @Override
                public void onSuccess(int responseCode, Object object) {
                    switch (responseCode) {
                        case 200:
                            JSONObject jsonObject = (JSONObject) object;
                            try {
                                jwtLogin = (jsonObject.getJSONObject("response").getString("jwt"));
                                System.out.println(jwtLogin);
                                usuarioLogado = JWTUtils.setUserJWT(getContext(), jwtLogin);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println();
                            break;
                        default:
                            Toast.makeText(getContext(), "Response code não tratado", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getContext(), "Falha no request", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void disableForm(){
        emailLayout.setEnabled(false);
        senhaLayout.setEnabled(false);
        buttonLogin.setEnabled(false);
        othersLayout.setVisibility(View.GONE);

//        tryingText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void enableForm(){
        emailLayout.setEnabled(true);
        senhaLayout.setEnabled(true);
        buttonLogin.setEnabled(true);
        othersLayout.setVisibility(View.VISIBLE);

//        tryingText.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

}