package matthew.motionmine;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {


    public String infobody;
    public String infotag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        infobody = "When the 'Arm Mine' button is hit, the user is given 3 seconds to place the phone in an enticing area. When an unsuspecting thief comes to pick up your phone, the on-board accelerometer will sense the movement and explode!";

        infotag = "Keep Calm and Scare Your Friends!";

        TextView infotxt = (TextView) findViewById(R.id.infotxt);
        infotxt.setTextColor(Color.WHITE);
        infotxt.setText(infobody);


        TextView infotxttag = (TextView) findViewById(R.id.infotag);
        infotxttag.setTextColor(Color.BLACK);
        infotxttag.setText(infotag);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }
}
