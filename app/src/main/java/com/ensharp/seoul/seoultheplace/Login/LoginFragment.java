package com.ensharp.seoul.seoultheplace.Login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.ensharp.seoul.seoultheplace.Login.KakaoLogin.GlobalApplication;
import com.ensharp.seoul.seoultheplace.Login.KakaoLogin.SessionCallback;
import com.ensharp.seoul.seoultheplace.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

public class LoginFragment extends android.support.v4.app.Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {


    static LoginBackgroundActivity LActivity;

    GoogleApiClient mGoogleApiClient;
    SignInButton googleLoginBtn;
    LoginButton kakaoLoginBtn;

    Button makeID;
    boolean alreadyOpen = false;

    int RC_SIGN_IN = 1000;

    private GlobalApplication globalApplication;
    private SessionCallback sessionCallback;

    public static String email = null;
    public static String name = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_first,null); //view를 불러온다.

        LActivity = (LoginBackgroundActivity) getActivity();

        kakaoLoginBtn = (LoginButton)view.findViewById(R.id.kakao_login);
        kakaoLoginBtn.setOnClickListener(this);

        googleLoginBtn = (SignInButton)view.findViewById(R.id.google_login);
        googleLoginBtn.setSize(SignInButton.SIZE_STANDARD);
        googleLoginBtn.setOnClickListener(this);

        globalApplication = new GlobalApplication();
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

        makeID = (Button)view.findViewById(R.id.newID);
        makeID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LActivity.onFragmentChanged();
            }
        });

        if(!alreadyOpen) {
            setGoogleLogin();
        }
        return view;//view를 불렀으니 view를 돌려준다.
    }

    //구글로그인 세팅을 합니다.
    private void setGoogleLogin() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleLoginBtn.setScopes(gso.getScopeArray());
        alreadyOpen = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_login:
                googleSignIn();
                break;
        }
    }
    public static void kakaoSignIn(){
        LActivity.onFragmentChanged();
    }

    private void googleSignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //구글로그인 결과를 뿌려줍니다.
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            name = acct.getDisplayName();
            email = acct.getEmail();
            LActivity.onFragmentChanged();
        }
    }

    //연결이 끊겼을때 토스트 메세지로 표시해 줍니다.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


}
