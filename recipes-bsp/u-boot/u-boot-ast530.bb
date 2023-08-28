require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
DEPENDS:append = " bc-native u-boot-tools-native python3-setuptools-native"

FORK = "andestech"
SRCREV = "777ecdea665976baff7410dc45ac0fba5820f5e8"
BRANCH = "ast-v5_3_0-branch"
SRC_URI = " \
    git://github.com/${FORK}/uboot.git;protocol=https;branch=${BRANCH} \
    file://0001-board-ae350-Add-missing-env-variables-for-booti.patch \
    file://0002-vbe-Allow-probing-the-VBE-bootmeth-to-fail-in-OS-fix.patch \
    file://mmc-support.cfg \
    file://opensbi-options.cfg \
    file://display-info.cfg \
    file://tftp-mmc-boot.txt \
    file://uEnv.txt \
    "

do_compile[depends] += "opensbi-ast530:do_deploy"

do_compile:prepend() {
    export OPENSBI=${DEPLOY_DIR_IMAGE}/fw_dynamic.bin
}

# Overwrite this for your server
TFTP_SERVER_IP ?= "127.0.0.1"

do_configure:prepend() {
    if [ -f "${WORKDIR}/tftp-mmc-boot.txt" ]; then
        sed -i -e 's,@SERVERIP@,${TFTP_SERVER_IP},g' ${WORKDIR}/tftp-mmc-boot.txt
        mkimage -A riscv -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/tftp-mmc-boot.txt ${WORKDIR}/boot.scr.uimg
    fi
}

do_deploy:append() {
    if [ -f "${WORKDIR}/boot.scr.uimg" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/boot.scr.uimg ${DEPLOYDIR}
    fi

    if [ -f "${WORKDIR}/uEnv.txt" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 644 ${WORKDIR}/uEnv.txt ${DEPLOYDIR}
    fi
}
