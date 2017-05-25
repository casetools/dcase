Instructions for DC-ASE(Design for Context-Aware Systems Engineering)
======

====== DC-ASE DEVELOPERS ===============
0. Prerequisites
	Eclipse RCP neon with Maven (M2e) and Git (EGit) plugins correctly installed

1. Clone this repository to your local harddrive.
2. Open an Eclipse RCP neon instance in your preferred workspace (different than the folder where you have downloaded your repository)
3. Click on Import -> Existing Maven Projects -> (Select as root directory the folder where you have cloned your repo). EGit should automatically detect 
that you are using a repository.
4. Left click on the folder -> Run as -> 7 Maven install
3. Enjoy. =)

====== DC-ASE USERS ===============
0. Prerequisites
	Modelio 3.5.1.a Notice that it only supports this version of Modelio. 

1. Copy the file dcase/target/dcase-*.jmdac 
2. Open Modelio 3.5.1.a	and click on Configuration -> Modules Catalog... -> Add a module to the catalog...
3. Paste the jmdac file on the folder that is opened by the file chooser.
4. Create new/Open existing project 
5. Click on Configuration -> Modules... 
	5.1 Display the modules catalog by clicking on the four arrowed icon (Open modules catalog)
6. Add DCase [VERSION NUMBER] to the project by clicking on the green plus icon (Add module to project)
7. Close the project configuration
8. Enjoy. =)