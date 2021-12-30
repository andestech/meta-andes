require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc
LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

FILESEXTRAPATHS:prepend := "${THISDIR}/files/ae350-ax45mp:"
DEPENDS:append = " bc dtc opensbi u-boot-tools-native"

SRCREV="050acee119b3757fee3bd128f55d720fdd9bb890"

SRC_URI = " \
    git://git.denx.de/u-boot.git;branch=master \
    file://0001-Fix-mmc-no-partition-table-error.patch \
    file://0002-Fix-AX45MP-XIP-mode-booting-fail-on-VCU118-issue.patch \
    file://0003-Enable-prints-during-boot.patch \
    file://0004-Enable-cache-in-uboot-spl.patch \
    file://0005-prevent-fw_dynamic-relocation.patch \
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
