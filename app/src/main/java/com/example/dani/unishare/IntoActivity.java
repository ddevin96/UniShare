package com.example.dani.unishare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntoActivity extends AppIntro {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String title = "UNISHARE";
    String description = "Sei uno studente Erasmus e non sai a chi "
            + "rivolgerti per saperne di più sulla tua esperienza, "
            + "oppure hai già vissuto esperienze del genere e "
            + "vuoi dare una mano ai novellini?"
            + "Unishare è la soluzione!;)";
    SliderPage sliderPage = new SliderPage();
    sliderPage.setTitle(title);
    sliderPage.setDescription(description);
    //sliderPage.setBgColor(Integer.parseInt("@color/nero"));
    addSlide(AppIntroFragment.newInstance(sliderPage));

    String title1 = "UNISHARE";
    String description1 = "Se non sei registrato puoi consultare "
            + "i post e i commenti degli altri utenti in base alla bacheca che ti interessa.";
    SliderPage sliderPage1 = new SliderPage();
    sliderPage1.setTitle(title1);
    sliderPage1.setDescription(description1);
    //sliderPage.setBgColor(Integer.parseInt("@color/nero"));
    addSlide(AppIntroFragment.newInstance(sliderPage1));

    String title2 = "UNISHARE";
    String description2 = "Se vuoi pubblicare post o commenti devi essere registrato."
            + "Per registrarti clicca sul menù in alto a destra e seleziona la voce Registrati";
    SliderPage sliderPage2 = new SliderPage();
    sliderPage2.setTitle(title2);
    sliderPage2.setDescription(description2);
    //sliderPage.setBgColor(Integer.parseInt("@color/nero"));
    addSlide(AppIntroFragment.newInstance(sliderPage2));

    String title3 = "UNISHARE";
    String description3 = "Una volta registrato avrai un profilo tutto tuo!"
            + "Potrai cercare i contatti che ti interessano.";
    SliderPage sliderPage3 = new SliderPage();
    sliderPage3.setTitle(title3);
    sliderPage3.setDescription(description3);
    //sliderPage.setBgColor(Integer.parseInt("@color/nero"));
    addSlide(AppIntroFragment.newInstance(sliderPage3));

    String title4 = "UNISHARE";
    String description4 = "BENVENUTO SU UNISHARE!"
            + "Let's start!";
    SliderPage sliderPage4 = new SliderPage();
    sliderPage4.setTitle(title4);
    sliderPage4.setDescription(description4);
    //sliderPage.setBgColor(Integer.parseInt("@color/nero"));
    addSlide(AppIntroFragment.newInstance(sliderPage4));
  }

  @Override
  public void onSkipPressed(Fragment currentFragment) {
    super.onSkipPressed(currentFragment);
    // Do something when users tap on Skip button.
  }

  @Override
  public void onDonePressed(Fragment currentFragment) {
    super.onDonePressed(currentFragment);
    startActivity(new Intent(getApplicationContext(), MainActivity.class));
  }

  //ciao1
  @Override
  public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
    super.onSlideChanged(oldFragment, newFragment);
    // Do something when the slide changes.
  }
}
