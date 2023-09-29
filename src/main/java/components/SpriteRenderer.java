package components;

import yuba.Component;

public class SpriteRenderer extends Component {

    private boolean firstTime = false;
    @Override
    public void start() {
        super.start();
        System.out.println("I am starting...");
    }

    @Override
    public void update(float dt) {
        if (!firstTime){
            System.out.println("I am updating");
            firstTime = true;
        }
    }
}
