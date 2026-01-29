package engine.Temp;

import engine.Behaviors.Component;
import engine.Engine_Classes.Time;
import engine.structs.Quaternion;

public class Spinner extends Component {
    
    @Override
    public void update() {
        transform().Rotation = (Quaternion) transform().Rotation.mul(Quaternion.Euler(0, 5 * Time.deltaTime, 0));
    }
}
