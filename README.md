# Andes OpenEmbedded Layer

```
mkdir riscv-andes && cd riscv-andes
repo init -u git://github.com/andestech/meta-andes -b ast-v5_0_0-branch -m tools/manifests/andes.xml
repo sync
repo start work --all
. ./meta-andes/setup.sh
export LOCAL_SRC="/path/to/local/source"
bitbake core-image-full-cmdline
gunzip -c <IMAGE>.wic.gz | sudo dd of=/dev/sdX bs=4M iflag=fullblock oflag=direct conv=fsync status=progress
```

Reset the board via GDB

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
