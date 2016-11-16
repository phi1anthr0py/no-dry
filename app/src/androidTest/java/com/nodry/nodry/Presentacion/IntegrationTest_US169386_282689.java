package com.nodry.nodry.Presentacion;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.nodry.nodry.Datos.Gasolinera;
import com.nodry.nodry.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * US-169386
 * TASK 282689
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IntegrationTest_US169386_282689 {

    List<Gasolinera> listaGasolineras;

    @Rule
    public ActivityTestRule<DetailsActivity> mActivityDetailsTestRule = new ActivityTestRule<>(DetailsActivity.class, true, false);

    @Test
    public void testIntent() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        listaGasolineras = new ArrayList<Gasolinera>();
        for(int i=0;i<2;i++){
            Gasolinera gas = new Gasolinera(i,"Santander","Cantabria","Calle Falsa 1 2 3",0.97+i,1.07,"Gasolinera "+i,1.06+i,1.15,"L-D: 08:00-21:00",43.395944,-4.155194);
            listaGasolineras.add(gas);
        }
        Intent intent = new Intent(targetContext, DetailsActivity.class);
        intent.putExtra("CCAA",06);
        intent.putExtra("IDEESS", listaGasolineras.get((int)1).getIDEESS());
        intent.putExtra("listaGasolineras",(Serializable)listaGasolineras);
        mActivityDetailsTestRule.launchActivity(intent);

    // Pruebas realizadas sobre el Activity

        //Icono Existe
        onView(withId(R.id.imageView_icono)).check(matches(isDisplayed()));
        //Rotulo existe y su nombre coincide
        onView(withId(R.id.textView_Rotulo)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_Rotulo)).check(matches(withText(listaGasolineras.get(1).getRotulo())));
        //Localidad existe y su etiqueta coincide
        onView(withId(R.id.textView_Localidad)).check(matches(withText(listaGasolineras.get(1).getLocalidad())));
        onView(withId(R.id.lbl_Localidad)).check(matches(withText("Localidad:")));
        //Provincia existe y su etiqueta coincide
        onView(withId(R.id.textView_Provincia)).check(matches(withText(listaGasolineras.get(1).getProvincia())));
        onView(withId(R.id.lbl_Provincia)).check(matches(withText("Provincia:")));
        //Dirección existe y su etiqueta coincide
        onView(withId(R.id.lbl_Direccion)).check(matches(withText("Dirección:")));
        onView(withId(R.id.textView_Direccion)).check(matches(isDisplayed()));
        //Los campos de los diferentes combustibles existen
        onView(withId(R.id.textView_Gasolina95)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_Gasolina98)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_Diesel)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_DieselPlus)).check(matches(isDisplayed()));
        //Las etiquetas de los diferentes combustibles coinciden
        onView(withId(R.id.lbl_Gasolina95)).check(matches(withText("Sin plomo 95:")));
        onView(withId(R.id.lbl_Gasolina98)).check(matches(withText("Sin plomo 98:")));
        onView(withId(R.id.lbl_Diesel)).check(matches(withText("Diesel:")));
        onView(withId(R.id.lbl_DieselPlus)).check(matches(withText("Diesel Plus:")));
        //Los botones aparecen para las gasolineras más baratas
        onView(withId(R.id.button_Diesel)).check(matches(isDisplayed()));
        onView(withId(R.id.button_DieselPlus)).check(matches(isDisplayed()));
        //El horario se muestra correctamente
        onView(withId(R.id.textView_Horario)).check(matches(withText(listaGasolineras.get(1).getHorario())));



    }

}