# Andes OpenEmbedded Layer

This is a BSP layer depends on:

```
URI: git@github.com:openembedded/openembedded-core.git
branch: honister

URI: git@github.com:openembedded/meta-openembedded.git
branch: honister

URI: git@github.com:riscv/meta-riscv.git
branch: master
```

## Creating Workspace

```
mkdir riscv-andes && cd riscv-andes
repo init -u git://github.com/andestech/meta-andes -b ast-v5_0_0-branch -m tools/manifests/andes.xml
repo sync
```

Track the changes to the layers.

```
repo start work --all
```

This command is equivalent to `git checkout -b work` based on revisions specified in `meta-andes/tools/manifests/andes.xml`.

## Initializing Build Directory

Run `setup.sh`, a build directory will be created and add `meta-andes` layer automatically.

```
. ./meta-andes/setup.sh
```

Extract Linux and OpenSBI tarballs from AndeSight™ v5.0.0 packages to a local directory.

```
/path/to/source/code/
	|- linux-5.4/
	`- opensbi/
```

Export the path as environment variable `LOCAL_SRC`.

```
export LOCAL_SRC="/path/to/source/code/"
```

Start the build process, at least 80 GB of files will be generated, please prepare about 100 GB of hard disk space.

```
bitbake core-image-full-cmdline
```

> If BitBake consumes too much computing resources, `BB_NUMBER_THREADS` and `PARALLEL_MAKE` can be used to limit the number of parallel tasks. e.g. `PARALLEL_MAKE="-j 4" BB_NUMBER_THREADS=4 bitbake core-image-full-cmdline`
> * `BB_NUMBER_THREADS`: Number of parallel BitBake tasks
> * `PARALLEL_MAKE`: Number of parallel processes

The resulting image and binaries reside in `$BUILDDIR/tmp-glibc/deploy/images/ae350-ax45mp`.

## Flashing Image to SD Card

Use Linux `dd` command to flash the image.

```
gunzip -c <IMAGE>.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress
```

On Windows and macOS, [belenaEther](https://www.balena.io/etcher/) provides GUI to flash the image.

<img src="https://i.imgur.com/W7YZc8j.png" width="450px" />

Insert SD card and reset the board and access serial console with baud `38400/8-N-1`, should boot Linux from mmc.

## Generating Cross Toolchain

Run `bitbake meta-toolchain` to generate the cross toolchain installer script under `$BUILDDIR/tmp-glibc/deploy/sdk`.
Once the cross toolchain has been setup, gcc and gdb can be accessed by `$CC` and `$GDB`.

> SDK environment variables. See `environment-setup-riscv64-oe-linux` file for more details.
> * `$CC`: C Compiler
> * `$CFLAGS`: C flags
> * `$CXX`: C++ compiler
> * `$LD`: Linker
> * `$AS`: Assembler
> * `$GDB`: GNU Debugger
> * `$OBJDUMP`: objdump

### Reset the board via GDB

Set `<TARGET_IP>` to the ICEman host IP address.

```
$GDB -ex "target remote <TARGET_IP>:<PORT_NUMBER>" \
     -ex "set confirm off" \
     -ex "set pagination off" \
     -ex "monitor reset halt" \
     -ex "set \$ra=0" \
     -ex "set \$sp=0" \
     -ex "flushregs" \
     -ex "c"
```
