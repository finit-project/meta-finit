# meta-finit
OpenEmbedded/Yocto layer for [finit](https://github.com/finit-project/finit)


# Dependencies

```
URI: git://git.yoctoproject.org/poky
```


# Build a image choosing finit as the init manager

Install the `kas` tool (optionally, you can install globally for all users. Run as `root`, respectively under `sudo` then):
```
pip install kas
```

Build a image within KAS:
```
$ mkdir ~/finit-workspace
$ cd ~/finit-workspace
$ git clone https://github.com/liuming50/meta-finit.git
$ kas build ~/finit-workspace/meta-finit/kas/qemuarm64.yml
```

Test the image in QEMU:
```
$ kas shell ~/finit-workspace/meta-finit/kas/qemuarm64.yml -c 'runqemu nographic qemuarm64'
```

The above command will ask for your sudo password to set up QEMU network, after you will get a console for loging in with 'root', no password needed.


Layer Maintainer: [Ming Liu](<liu.ming50@gmail.com>)
