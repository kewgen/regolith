git --version

git clone -b develop https://github.com/kewgen/awt.git awt
git clone -b master https://github.com/kewgen/regolith.git regolith

git remote -v

cd awt
git pull
cd ..

cd regolith
git pull
cd ..
