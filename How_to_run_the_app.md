1.  open a terminal, navigate to the `gcom` project root folder.
Run `sh registry.sh`  in the terminal then press `ctrl + z` let the process run in the background

2. In the same terminal, run `sh naming.sh`, press `ctrl + z`  as first step, force the current process to run in the background.
3. In the same terminal, run `sh main.sh` , this shell script will open the GUI of the GCom app, type a user name and click login to create a user.  press `ctrl + z` and run `sh main.sh` again to create another user. 
4. To test if the app can still multicast message without using naming service, go back to the shell and kill the corresponding process of naming.sh.  
5. For all the other tests you can just simply follower the buttons on GUI, and test different functions. 
6. Debug mode is triggered by "Debug" Button in Chat windows. To check the backend data of deliverd message, click the deliver button in debug window.  