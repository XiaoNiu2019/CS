# CS

xniu@xniu-XPS-13-9380:~/Xiao/git$ echo "# CS" >> README.md
xniu@xniu-XPS-13-9380:~/Xiao/git$ git init
Initialized empty Git repository in /home/xniu/Xiao/git/.git/
xniu@xniu-XPS-13-9380:~/Xiao/git$ git add README.md
xniu@xniu-XPS-13-9380:~/Xiao/git$ git commit -m "first commit"

*** Please tell me who you are.

Run

  git config --global user.email "you@example.com"
  git config --global user.name "Your Name"

to set your account's default identity.
Omit --global to set the identity only in this repository.

fatal: unable to auto-detect email address (got 'xniu@xniu-XPS-13-9380.(none)')
xniu@xniu-XPS-13-9380:~/Xiao/git$ ^C
xniu@xniu-XPS-13-9380:~/Xiao/git$ git config --global user.email "neo.polyisae@gmail.com"
xniu@xniu-XPS-13-9380:~/Xiao/git$ git config --global user.name "Kaitoukiddo1314"
xniu@xniu-XPS-13-9380:~/Xiao/git$ git commit -m "first commit"
[master (root-commit) ece2b71] first commit
 1 file changed, 1 insertion(+)
 create mode 100644 README.md
xniu@xniu-XPS-13-9380:~/Xiao/git$ git remote add origin https://github.com/XiaoNiu2019/CS.git
xniu@xniu-XPS-13-9380:~/Xiao/git$ git push -u origin master
Username for 'https://github.com': Kaitoukiddo1314
Password for 'https://Kaitoukiddo1314@github.com': 
remote: Invalid username or password.
fatal: Authentication failed for 'https://github.com/XiaoNiu2019/CS.git/'
xniu@xniu-XPS-13-9380:~/Xiao/git$ git push -u origin master
Username for 'https://github.com': XiaoNiu2019
Password for 'https://XiaoNiu2019@github.com': 
Counting objects: 3, done.
Writing objects: 100% (3/3), 224 bytes | 224.00 KiB/s, done.
Total 3 (delta 0), reused 0 (delta 0)
To https://github.com/XiaoNiu2019/CS.git
 * [new branch]      master -> master
Branch 'master' set up to track remote branch 'master' from 'origin'.
xniu@xniu-XPS-13-9380:~/Xiao/git$ git push -u origin master
Username for 'https://github.com': XiaoNiu2019
Password for 'https://XiaoNiu2019@github.com': 
Branch 'master' set up to track remote branch 'master' from 'origin'.
Everything up-to-date
xniu@xniu-XPS-13-9380:~/Xiao/git$ ^C
xniu@xniu-XPS-13-9380:~/Xiao/git$ git add -m 
Project_Snake/ .Rhistory      
xniu@xniu-XPS-13-9380:~/Xiao/git$ git add -m Project_Snake/
error: unknown switch `m'
usage: git add [<options>] [--] <pathspec>...

    -n, --dry-run         dry run
    -v, --verbose         be verbose

    -i, --interactive     interactive picking
    -p, --patch           select hunks interactively
    -e, --edit            edit current diff and apply
    -f, --force           allow adding otherwise ignored files
    -u, --update          update tracked files
    --renormalize         renormalize EOL of tracked files (implies -u)
    -N, --intent-to-add   record only the fact that the path will be added later
    -A, --all             add changes from all tracked and untracked files
    --ignore-removal      ignore paths removed in the working tree (same as --no-all)
    --refresh             don't add, only refresh the index
    --ignore-errors       just skip files which cannot be added because of errors
    --ignore-missing      check if - even missing - files are ignored in dry run
    --chmod <(+/-)x>      override the executable bit of the listed files

xniu@xniu-XPS-13-9380:~/Xiao/git$ git add -A -m Project_Snake/
error: unknown switch `m'
usage: git add [<options>] [--] <pathspec>...

    -n, --dry-run         dry run
    -v, --verbose         be verbose

    -i, --interactive     interactive picking
    -p, --patch           select hunks interactively
    -e, --edit            edit current diff and apply
    -f, --force           allow adding otherwise ignored files
    -u, --update          update tracked files
    --renormalize         renormalize EOL of tracked files (implies -u)
    -N, --intent-to-add   record only the fact that the path will be added later
    -A, --all             add changes from all tracked and untracked files
    --ignore-removal      ignore paths removed in the working tree (same as --no-all)
    --refresh             don't add, only refresh the index
    --ignore-errors       just skip files which cannot be added because of errors
    --ignore-missing      check if - even missing - files are ignored in dry run
    --chmod <(+/-)x>      override the executable bit of the listed files

xniu@xniu-XPS-13-9380:~/Xiao/git$ git add -A Project_Snake/
xniu@xniu-XPS-13-9380:~/Xiao/git$ git commit -m "Project Java Snake"
[master 823d1c1] Project Java Snake
 42 files changed, 3268 insertions(+)
 create mode 100644 Project_Snake/.classpath.xml
 create mode 100644 Project_Snake/.project.xml
 create mode 100644 Project_Snake/.settings/org.eclipse.jdt.core.prefs
 create mode 100644 Project_Snake/bin/SnakeGame/BotSnake.class
 create mode 100644 Project_Snake/bin/SnakeGame/BotSnakeNode.class
 create mode 100644 Project_Snake/bin/SnakeGame/BotSnake_Random.class
 create mode 100644 Project_Snake/bin/SnakeGame/BotSnake_Smart.class
 create mode 100644 Project_Snake/bin/SnakeGame/BotSnake_SuperSmart.class
 create mode 100644 Project_Snake/bin/SnakeGame/Box.class
 create mode 100644 Project_Snake/bin/SnakeGame/Controller.class
 create mode 100644 Project_Snake/bin/SnakeGame/Game$GameTimerTask.class
 create mode 100644 Project_Snake/bin/SnakeGame/Game.class
 create mode 100644 Project_Snake/bin/SnakeGame/Keyboard.class
 create mode 100644 Project_Snake/bin/SnakeGame/LocalSnake.class
 create mode 100644 Project_Snake/bin/SnakeGame/Map.class
 create mode 100644 Project_Snake/bin/SnakeGame/PlayerSnake.class
 create mode 100644 Project_Snake/bin/SnakeGame/PlayerSnakeNode.class
 create mode 100644 Project_Snake/bin/SnakeGame/Position.class
 create mode 100644 Project_Snake/bin/SnakeGame/ServerSnake.class
 create mode 100644 Project_Snake/bin/SnakeGame/ServerSnakeNode.class
 create mode 100644 Project_Snake/bin/SnakeGame/ServerSnake_Opponent.class
 create mode 100644 Project_Snake/bin/SnakeGame/Snake.class
 create mode 100644 Project_Snake/bin/SnakeGame/SnakeGameMain.class
 create mode 100644 Project_Snake/src/SnakeGame/BotSnake.java
 create mode 100644 Project_Snake/src/SnakeGame/BotSnakeNode.java
 create mode 100644 Project_Snake/src/SnakeGame/BotSnake_Random.java
 create mode 100644 Project_Snake/src/SnakeGame/BotSnake_Smart.java
 create mode 100644 Project_Snake/src/SnakeGame/BotSnake_SuperSmart.java
 create mode 100644 Project_Snake/src/SnakeGame/Box.java
 create mode 100644 Project_Snake/src/SnakeGame/Controller.java
 create mode 100644 Project_Snake/src/SnakeGame/Game.java
 create mode 100644 Project_Snake/src/SnakeGame/Keyboard.java
 create mode 100644 Project_Snake/src/SnakeGame/LocalSnake.java
 create mode 100644 Project_Snake/src/SnakeGame/Map.java
 create mode 100644 Project_Snake/src/SnakeGame/PlayerSnake.java
 create mode 100644 Project_Snake/src/SnakeGame/PlayerSnakeNode.java
 create mode 100644 Project_Snake/src/SnakeGame/Position.java
 create mode 100644 Project_Snake/src/SnakeGame/ServerSnake.java
 create mode 100644 Project_Snake/src/SnakeGame/ServerSnakeNode.java
 create mode 100644 Project_Snake/src/SnakeGame/ServerSnake_Opponent.java
 create mode 100644 Project_Snake/src/SnakeGame/Snake.java
 create mode 100644 Project_Snake/src/SnakeGame/SnakeGameMain.java
xniu@xniu-XPS-13-9380:~/Xiao/git$ git push -u mat
fatal: 'mat' does not appear to be a git repository
fatal: Could not read from remote repository.

Please make sure you have the correct access rights
and the repository exists.
xniu@xniu-XPS-13-9380:~/Xiao/git$ git push -u master
fatal: 'master' does not appear to be a git repository
fatal: Could not read from remote repository.

Please make sure you have the correct access rights
and the repository exists.
xniu@xniu-XPS-13-9380:~/Xiao/git$ git push -u origin master
Username for 'https://github.com': XiaoNiu2019
Password for 'https://XiaoNiu2019@github.com': 
remote: Invalid username or password.
fatal: Authentication failed for 'https://github.com/XiaoNiu2019/CS.git/'
xniu@xniu-XPS-13-9380:~/Xiao/git$ git push -u origin master
Username for 'https://github.com': XiaoNiu2019
Password for 'https://XiaoNiu2019@github.com': 
Counting objects: 50, done.
Delta compression using up to 8 threads.
Compressing objects: 100% (48/48), done.
Writing objects: 100% (50/50), 54.43 KiB | 7.77 MiB/s, done.
Total 50 (delta 9), reused 0 (delta 0)
remote: Resolving deltas: 100% (9/9), done.
To https://github.com/XiaoNiu2019/CS.git
   ece2b71..823d1c1  master -> master
Branch 'master' set up to track remote branch 'master' from 'origin'.
xniu@xniu-XPS-13-9380:~/Xiao/git$ 
