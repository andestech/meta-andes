require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
DEPENDS:append = " bc-native u-boot-tools-native python3-setuptools-native"

SRCREV = "f06aae7a25901a6a2d6815fa98dab12910ebfc90"
BRANCH = "ast-v5_2_0-branch"
SRC_URI = " \
    git:///work/u-boot;protocol=file;branch=${BRANCH} \
    file://mmc-support.cfg \
    file://opensbi-options.cfg \
    file://tftp-mmc-boot.txt \
    file://uEnv.txt \
    "

do_compile[depends] += "opensbi-ast520:do_deploy"

do_compile:prepend:ae350-ax45mp() {
    export OPENSBI=${DEPLOY_DIR_IMAGE}/fw_dynamic.bin
}

# Overwrite this for your server
TFTP_SERVER_IP ?= "127.0.0.1"

do_configure:prepend:ae350-ax45mp() {
    if [ -f "${WORKDIR}/tftp-mmc-boot.txt" ]; then
        sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/tftp-mmc-boot.txt
        mkimage -A riscv -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/tftp-mmc-boot.txt ${WORKDIR}/boot.scr.uimg
    fi
}

do_deploy:append:ae350-ax45mp() {
    if [ -f "${WORKDIR}/boot.scr.uimg" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOYDIR}
    fi

    if [ -f "${WORKDIR}/uEnv.txt" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 644 ${WORKDIR}/uEnv.txt ${DEPLOYDIR}
    fi
}
