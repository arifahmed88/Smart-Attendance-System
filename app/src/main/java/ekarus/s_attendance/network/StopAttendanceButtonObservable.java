/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ekarus.s_attendance.network;



import java.util.Observable;

/**
 * @author Arefin
 */
public class StopAttendanceButtonObservable extends Observable {

    public void buttonPressed() {

        setChanged();
        notifyObservers();
        clearChanged();
    }
}
