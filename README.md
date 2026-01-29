Hello and welcome to the J3DEngine git repo
This is an in development engine written primaraly in the Java language that takes heavy inspiration from Unity engine. This engine was made as a project to learn how engine backends actualy work

This engine is provided by 09glich as is for any user to use at no cost

To actualy run the engine find the java file called StarterApp.java and write your scene settup in there. I am currently working on getting a scene system working. ImGUI is also included in the project but currently does not work.

All Current Features that are being worked on and may occasionaly act strangely

The full implimentation of the OpenGL Graphics pipeline
Component workflow is being tweeked

All Current Features include

3D Rendering Meshes, Materials, Shaders Excluding Images (In Progress)
Component Based Scene workflow (Simalar to unity engine)
Custom Components can be added to the system
Time Class. Delta time Engine Time. Game time


Future features will include the folowing

Anything with the (As Package) tag means it will not be built into the engine rather than as an addition to the engine as an optional feature.
Also note that some things will change from packages to forced implimentations if the package alone cannot be contained to high end objects

GUID Asset system
Component Serialization
Full PBR Lighting base Shaders
Event based user input module
Physics system implimentation (As Package)
FMod implimentation (As Package) 
OpenXR implimentation (As Package) (More than likely going to have to skim package here)
Dear ImGUI Game Editor. (The engine is going to make the engines editor :D)
Networking Package
Future rendering pipelines that include Vulkan and metal and posibly more.

Thank you for checking out the project. I made this for fun as i was wondering how the backend of unity worked and i came up with this.
