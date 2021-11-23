#!/bin/bash
# Bootstrapper for buildbot slave

DIR="build"
MACHINE="ae350-ax45mp"
CONFFILE="conf/auto.conf"
BITBAKEIMAGE="core-image-full-cmdline"

# make sure sstate is there
#echo "Creating sstate directory"
#mkdir -p ~/sstate/$MACHINE

# fix permissions set by buildbot
#echo "Fixing permissions for buildbot"
#umask -S u=rwx,g=rx,o=rx
#chmod -R 755 .

# Reconfigure dash on debian-like systems
which aptitude > /dev/null 2>&1
ret=$?
if [ "$(readlink /bin/sh)" = "dash" -a "$ret" = "0" ]; then
  sudo aptitude install expect -y
  expect -c 'spawn sudo dpkg-reconfigure -freadline dash; send "n\n"; interact;'
elif [ "${0##*/}" = "dash" ]; then
  echo "dash as default shell is not supported"
  return
fi
# bootstrap OE
echo "Init OE"
export BASH_SOURCE="openembedded-core/oe-init-build-env"
. ./openembedded-core/oe-init-build-env $DIR

# Symlink the cache
#echo "Setup symlink for sstate"
#ln -s ~/sstate/${MACHINE} sstate-cache

# add the missing layers
echo "Adding layers"
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-multimedia
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-riscv
bitbake-layers add-layer ../meta-andes

# fix the configuration
echo "Creating auto.conf"

if [ -e $CONFFILE ]; then
    rm -rf $CONFFILE
fi
cat <<EOF > $CONFFILE
MACHINE ?= "${MACHINE}"
LOCAL_SRC ?= "/path/to/source"
#IMAGE_FEATURES += "tools-debug"
#IMAGE_FEATURES += "tools-tweaks"
#IMAGE_FEATURES += "dbg-pkgs"
# rootfs for debugging
#IMAGE_GEN_DEBUGFS = "1"
#IMAGE_FSTYPES_DEBUGFS = "tar.gz"
IMAGE_FEATURES:append = " debug-tweaks"
EXTRA_IMAGE_FEATURES:append = " ssh-server-dropbear"
EXTRA_IMAGE_FEATURES:append = " package-management"
PACKAGECONFIG:append:pn-qemu-native = " sdl"
PACKAGECONFIG:append:pn-nativesdk-qemu = " sdl"
USER_CLASSES ?= "buildstats buildhistory buildstats-summary image-prelink"

CORE_IMAGE_EXTRA_INSTALL += "htop vim tmux"

require conf/distro/include/no-static-libs.inc
require conf/distro/include/yocto-uninative.inc
require conf/distro/include/security_flags.inc

INHERIT += "uninative"

DISTRO_FEATURES:append = " largefile opengl ptest multiarch wayland pam systemd "
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
VIRTUAL-RUNTIME_init_manager = "systemd"
HOSTTOOLS_NONFATAL:append = " ssh"

DISTRO_NAME = "AndeSight"
DISTRO_VERSION = "2021.11"
DISTRO_CODENAME = "ast-v5.0.0"
EOF

echo "To build an image run"
echo "---------------------------------------------------"
echo "MACHINE=qemuriscv64 bitbake core-image-full-cmdline"
echo "---------------------------------------------------"
echo ""
echo "Buildable machine info"
echo "---------------------------------------------------"
echo "* qemuriscv64: The 64-bit RISC-V machine"
echo "* qemuriscv32: The 32-bit RISC-V machine"
echo "* ae350-ax45mp: AE350 example platform on the FPGA"
echo "---------------------------------------------------"

# start build
#echo "Starting build"
#bitbake $BITBAKEIMAGE
