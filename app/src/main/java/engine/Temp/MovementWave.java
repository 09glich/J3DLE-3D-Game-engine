package engine.Temp;

import engine.Engine_Classes.Time;

import engine.structs.*;

import java.util.concurrent.ThreadLocalRandom;


import engine.Behaviors.Component;

public class MovementWave extends Component {
    public Vector3 StartPosition;
    float offset = 0;

    @Override
    public void start() {
        StartPosition = transform().Position;
        offset = ThreadLocalRandom.current().nextFloat() * (float)(Math.PI * 2);
    }

    @Override 
    public void update() {
        transform().Position = (Vector3)StartPosition.add(new Vector3(0, ((float)Math.sin(Time.time + offset) * 20) * Time.deltaTime,0));
    }
}
