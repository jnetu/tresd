#version 400 core

in vec3 position;
//out vec3 colour;
in vec2 textureCoords;

out vec2 pass_textureCoords;

void main() {
    gl_Position = vec4(position, 1.0);
    //colour = vec3(position.x+0.5,1.0, position.y+0.5);
    pass_textureCoords = textureCoords;
}