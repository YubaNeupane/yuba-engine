package components;

import util.Settings;
import yuba.GameObject;
import yuba.MouseListener;
import yuba.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component{

    GameObject holdingObject = null;

    public void pickupObject(GameObject go){
        System.out.println(go);
        this.holdingObject = go;
        Window.getScene().addGameObjectToScene(go);
    }

    public void place(){
        this.holdingObject = null;
    }

    @Override
    public void update(float dt){
        if(holdingObject != null){
            holdingObject.transform.position.x = MouseListener.getOrthoX();
            holdingObject.transform.position.y = MouseListener.getOrthoY();

            holdingObject.transform.position.x = (int)(holdingObject.transform.position.x / Settings.GRID_WIDTH )* Settings.GRID_WIDTH;
            holdingObject.transform.position.y = (int)(holdingObject.transform.position.y / Settings.GRID_HEIGHT )* Settings.GRID_HEIGHT;


            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
    }


}
