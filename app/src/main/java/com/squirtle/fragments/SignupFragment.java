package com.squirtle.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.squirtle.R;
import com.squirtle.activities.MainActivity;
import com.squirtle.databinding.FragmentSignupBinding;
import com.squirtle.utils.JWTUtils;
import com.squirtle.utils.LogoutUtils;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;

    private TextInputLayout nomeLayout, sobrenomeLayout, emailLayout, senhaLayout, senhaLayoutConfirm ;
    private EditText nomeText, sobrenomeText, emailText, senhaText, senhaTextConfirm;
    private Button buttonSignup;

    private TextView tryingText;
    private ProgressBar progressBar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);

        nomeText = binding.nomeText;
        sobrenomeText = binding.sobrenomeText;
        emailText = binding.emailText;
        senhaText = binding.senhaText;
        senhaTextConfirm = binding.senhaTextConfirm;
        nomeLayout = binding.nomeLayout;
        sobrenomeLayout = binding.sobrenomeLayout;
        emailLayout = binding.emailLayout;
        senhaLayout = binding.senhaLayout;
        senhaLayoutConfirm = binding.senhaLayoutConfirm;
        buttonSignup = binding.buttonSignup;

        nomeText.setText("teste");
        sobrenomeText.setText("teste");
        emailText.setText("teste@teste.com");
        senhaText.setText("teste");
        senhaTextConfirm.setText("teste");


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String senha = senhaText.getText().toString();
                String confirmaSenha = senhaTextConfirm.getText().toString();
                if (senha.equals(confirmaSenha)){
                    signup();
                }else{
                    senhaText.setError("Verifique sua senha!");
                    senhaTextConfirm.setError("Verifique sua senha!");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//
    public void signup(){
        try {
            HttpAsync httpAsync = new HttpAsync(new URL(
                    getString(R.string.squirtle_api) + getString(R.string.squirtle_api_endpoint) +
                            getString(R.string.squirtle_api_endpoint_signup)));
            httpAsync.setDebug(true);
//            httpAsync.addParam("id", "1");
            httpAsync.addParam("nome", nomeText.getText().toString());
            httpAsync.addParam("sobrenome", sobrenomeText.getText().toString());
            httpAsync.addParam("email", emailText.getText().toString());
            httpAsync.addParam("senha", senhaText.getText().toString());
            httpAsync.post(new FutureCallback() {
                @Override
                public void onBeforeExecute() {
                    disableForm();
                }

                @Override
                public void onAfterExecute() {
                    Toast.makeText(getContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    enableForm();
                }

                @Override
                public void onSuccess(int responseCode, Object object) {
                    switch (responseCode) {
                        case 200:
                            JSONObject jsonObject = (JSONObject) object;
                            try {
                                System.out.println("chegou");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println();
                            break;
                        case 500:
                            // usuario com id já cadastrado
                            break;
                        default:
                            LogoutUtils.logout(getContext());
//                            Toast.makeText(getContext(), "Response code não tratado", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    LogoutUtils.logout(getContext());
//                    Toast.makeText(getContext(), "Falha no request", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void disableForm(){
        nomeLayout.setEnabled(false);
        sobrenomeLayout.setEnabled(false);
        emailLayout.setEnabled(false);
        senhaLayout.setEnabled(false);
        senhaLayoutConfirm.setEnabled(false);
        buttonSignup.setEnabled(false);
    }

    public void enableForm(){
        nomeLayout.setEnabled(true);
        sobrenomeLayout.setEnabled(true);
        emailLayout.setEnabled(true);
        senhaLayout.setEnabled(true);
        senhaLayoutConfirm.setEnabled(true);
        buttonSignup.setEnabled(true);

//        tryingText.setVisibility(View.GONE);
//        progressBar.setVisibility(View.GONE);
    }

}