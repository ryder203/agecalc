/* 
 Agecalc 1.7
 
 2019 by t-ryder & ize

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 3
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

 https://t-ryder.de
 
 */



package agecalc.android;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static Updater updater = new Updater();

    static class Updater implements Runnable {

        Activity activity = null;

        public Updater() {
            Thread updaterThread =  new Thread(this);
            updaterThread.setDaemon(true);
            updaterThread.start();
        }

        public synchronized void setActivity(Activity activity) {
            this.activity= activity;
            notifyAll();
        }

        @Override
        public synchronized void run() {
            while (true) {
                update();
                try {
                    wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        private void update() {
            final Activity activity = this.activity;
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Calendar todayMinus16 = Calendar.getInstance();
                        todayMinus16.add(Calendar.YEAR,-16);
                        SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy");

                        Calendar todayMinus18 = Calendar.getInstance();
                        todayMinus18.add(Calendar.YEAR,-18);

                        ((TextView) activity.findViewById(R.id.textView5)).setText(dateFormater.format(todayMinus16.getTime()));
                        ((TextView) activity.findViewById(R.id.textView9)).setText(dateFormater.format(todayMinus18.getTime()));

                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updater.setActivity(this);
    }
}

