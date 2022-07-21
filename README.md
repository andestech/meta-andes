# Andes OpenEmbedded Layer

This is a BSP layer depends on:

```
URI: git@github.com:openembedded/openembedded-core.git
branch: dunfell

URI: git@github.com:openembedded/meta-openembedded.git
branch: dunfell

URI: git@github.com:riscv/meta-riscv.git
branch: dunfell
```

## Building SD card image with kas-container

```
$ mkdir riscv-andes && cd riscv-andes
$ git clone https://github.com/andestech/meta-andes.git -b dunfell
$ wget https://raw.githubusercontent.com/siemens/kas/master/kas-container
$ chmod a+x ./kas-container
$ ./kas-container build meta-andes/ae350-ax45mp.yml
```

The generated SD card image will be located in the **build/tmp-glibc/deploy/images/<MACHINE>** directory.

## Running `bitbake` commands in kas shell

```
$ ./kas-container shell meta-andes/ae350-ax45mp.yml
...
builder@0d38e0a5f8c7:/build$ # bitbake command goes here
```

Some commonly used bitbake commands:
* `bitbake-layers show-layers`
* `bitbake <PACKAGE> -c listtasks`
* `bitbake <IMAGE> -c populate_sdk`

> See the [kas](https://kas.readthedocs.io/en/latest/userguide.html) documents for more details.

## Flashing Image to SD Card

Use Linux `dd` command to flash the image.

```
$ gunzip -c <IMAGE>.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress
```

On Windows and macOS, [belenaEther](https://www.balena.io/etcher/) provides GUI to flash the image.

<img src="https://i.imgur.com/W7YZc8j.png" width="450px" />

Insert SD card and access serial console with baud `38400/8-N-1`, then reset the board, should boot Linux from mmc.

## Generating Cross Toolchain

Run `bitbake <IMAGE> -c populate_sdk` to generate the cross toolchain installer script under **build/tmp-glibc/deploy/sdk**.
Once the cross toolchain has been extracted, gcc and gdb can be executed by `$CC` and `$GDB`.

> * `$CC`: C Compiler
> * `$CFLAGS`: C flags
> * `$CXX`: C++ compiler
> * `$LD`: Linker
> * `$AS`: Assembler
> * `$GDB`: GNU Debugger
> * `$OBJDUMP`: objdump
>
> Check more environment variables from `environment-setup-riscv64-oe-linux` file

### Reset the board via GDB

Set `<TARGET_IP>` to the ICEman host IP address.

```
$ $GDB -ex "target remote <TARGET_IP>:<PORT>" \
     -ex "set confirm off" \
     -ex "set pagination off" \
     -ex "monitor reset halt" \
     -ex "set \$ra=0" \
     -ex "set \$sp=0" \
     -ex "flushregs" \
     -ex "c"
```
