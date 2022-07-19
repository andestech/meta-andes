require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc
LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

FILESEXTRAPATHS:prepend := "${THISDIR}/files/ae350-ax45mp:"
DEPENDS:append = " bc dtc opensbi u-boot-tools-native python3-setuptools-native"

SRCREV="d637294e264adfeb29f390dfc393106fd4d41b17"

SRC_URI = " \
    git://git.denx.de/u-boot.git;branch=master \
    file://0001-Fix-mmc-no-partition-table-error.patch \
    file://0002-Prevent-fw_dynamic-from-relocation.patch \
    file://0003-Fix-u-boot-proper-booting-issue.patch \
    file://0004-Enable-printing-OpenSBI-boot-logo.patch \
    file://u-boot.cfg \
    "

SRC_URI:append:ae350-ax45mp = " \
    file://mmc-boot.txt \
"

do_compile:prepend:ae350-ax45mp() {
    export OPENSBI=${DEPLOY_DIR_IMAGE}/fw_dynamic.bin
}

do_configure:prepend:ae350-ax45mp() {
    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -A riscv -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr
    fi
}

do_deploy:append:ae350-ax45mp() {
    if [ -f "${WORKDIR}/boot.scr" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/boot.scr ${DEPLOY_DIR_IMAGE}
    fi
}

do_configure[depends] += "opensbi:do_deploy"
