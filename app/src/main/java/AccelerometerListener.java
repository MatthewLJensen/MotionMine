/**
 * Created by Matthew on 12/13/2015.
 */
public interface AccelerometerListener {

    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);

}
