package com.monitorfree.DI;

import android.provider.Settings;

import com.monitorfree.Activities.ForgotPassword;
import com.monitorfree.Activities.Login;
import com.monitorfree.Activities.Main2Activity;
import com.monitorfree.Activities.MonitorInfo;
import com.monitorfree.Activities.MyProfile;
import com.monitorfree.Activities.SaveMonitor;
import com.monitorfree.Activities.SignUp;
import com.monitorfree.Activities.Verification;
import com.monitorfree.BackgroundService.DemoService;
import com.monitorfree.Fragment.AddMonitor;
import com.monitorfree.Fragment.FAQs;
import com.monitorfree.Fragment.Home;
import com.monitorfree.MyApp;
import com.monitorfree.Splash;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jaspreet on 11/9/17.
 */

@Singleton
@Component(modules = {DIModule.class})
public interface DIComponants {


    void inject(DemoService demoService);

    static final class Initializer {
        private Initializer() {
        }

        public static DIComponants init(MyApp app) {
            return DaggerDIComponants.builder().dIModule(new DIModule(app))
                    .build();
        }
    }

    void inject(Login login);

    void inject(Home home);

//    void inject(SaveMonitor login);

    void inject(ForgotPassword login);

    void inject(Verification verification);

    void inject(Splash Splash);

    void inject(Main2Activity main2Activity);

//    void inject(SignUp login);

    void inject(MyProfile myProfile);

    void inject(FAQs myProfile);

    void inject(AddMonitor myProfile);

    void inject(MonitorInfo myProfile);

    void inject(Settings login);

}
