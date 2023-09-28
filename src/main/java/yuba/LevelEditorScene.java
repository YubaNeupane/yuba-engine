package yuba;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
            //position              // color                    // UV Coordinates
             100f,   0f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f,   1, 1, // Bottom Right
               0f, 100f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,   0, 0, // Top left
             100f, 100f, 0.0f,      1.0f, 0.0f, 1.0f, 1.0f,   1, 0, // Top Right
               0f,   0f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f,   0, 1, // Bottom Left
    };

    private int[] elementArray = {
            /*
                    x       x


                    x       x

             */
            2, 1, 0, // top right triangle
            0, 1, 3 // bottom left triangle

    };

    private int vaoID, vboID, eboID;
    private Texture testTexture;
    private Shader defaultShader;

    public  LevelEditorScene(){
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/testImage.png");


        /*
            Generate VAO, VBO, and EBO buffer objects and send to gpu
         */
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0,positionsSize, GL_FLOAT, false,vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize, GL_FLOAT, false, vertexSizeBytes,positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);


    }

    @Override
    public void update(float dt) {


        camera.position.x -= dt * 50.f;
        camera.position.y -= dt * 50.f;

        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER",0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMath4f("uProjection",camera.getProjectionMatrix());
        defaultShader.uploadMath4f("uView",camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        // Bind VAO
        glBindVertexArray(vaoID);

        // Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // unbine eveything

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

    }
}
