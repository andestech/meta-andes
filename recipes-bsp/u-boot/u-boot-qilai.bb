require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"
FILESEXTRAPATHS:prepend := "${THISDIR}/qilai:"
DEPENDS:append = " u-boot-tools-native bc-native dtc-native python3-setuptools-native"

FORK = "u-boot"
# e4b6ebd3de (v2022.04)
SRCREV = "e4b6ebd3de982ae7185dbf689a030e73fd06e0d2"
BRANCH = "master"
SRC_URI = " \
    git://github.com/${FORK}/u-boot.git;protocol=https;branch=${BRANCH} \
    file://0001-Add-Andes-PMA-support.patch \
    file://0002-Add-Andes-specific-SBI-calls-driver-patches-and-fixe.patch \
    file://0003-Add-new-Andes-board-and-CPU-support.patch \
    file://0004-Support-Fast-Boot-for-Andes-boards.patch \
    file://0005-Add-support-for-Andes-8-core-bitmap-and-L2-v1-memory.patch \
    file://0006-riscv-Modify-cache-disable-flow.patch \
    file://0007-riscv-ae350-Remove-jump-mode-fast-boot.patch \
    file://0008-riscv-ae350-Remove-payload-mode-fast-boot.patch \
    file://0009-riscv-ae350-Rename-dynamic-mode-fast-boot-default-co.patch \
    file://0010-riscv-ae350-Remove-redundant-default-configs-for-ae3.patch \
    file://0011-riscv-andes45-cpu-Add-software-Identification-for-EC.patch \
    file://0012-riscv-cpu-set-gp-before-board_init_f_init_reserve.patch \
    file://0013-riscv-Adjust-OpenSBI-load-address-to-0x0.patch \
    file://0014-board-andes25-ae350-Add-check-before-enabling-L2-cac.patch \
    file://0015-riscv-qilai-Remove-redundant-default-config-for-qila.patch \
    file://0016-riscv-Increase-CONFIG_SYS_INIT_SP_ADDR-36.patch \
    file://0017-riscv-Support-Qilai-from-Invecas.patch \
    file://0018-riscv-Enable-I-D-cache-47.patch \
    file://0019-riscv-qilai-Support-XIP-mode.patch \
    file://0020-riscv-qilai-Check-firmware_fdt_addr-validation.patch \
    file://0021-riscv-qilai-Correct-SYS_FDT_BASE.patch \
    file://0022-riscv-qilai-Support-larger-Image-for-Fast-Boot.patch \
    file://0023-riscv-qilai-add-wfi-for-NX27V.patch \
    file://0024-riscv-qilai-modify-fw_rsc_vdev_vring-memder-da-from-.patch \
    file://0025-riscv-qilai-add-openamp-ipi-extend-call.patch \
    file://0026-defconfig-remove-boot-flow-error-in-normal-boot-mode.patch \
    file://0027-qilai-fix-fdt-address-error-in-normal-boot-mode.patch \
    file://0028-spi-andes-atcspi200-1.-Fix-the-SSI-driver-always-usi.patch \
    file://0029-riscv-ae350-Move-the-DTB-in-front-of-kernel-50.patch \
    file://0030-sync-opensbi-vender-call.patch \
    file://0031-configs-fix-miss-spl_board_init-in-normal-boot.patch \
    file://0032-configs-remove-the-invecas-name-in-defconfig.patch \
    file://0033-libfdt-Fix-invalid-version-warning.patch \
    file://0034-riscv-Fix-build-against-binutils-2.38.patch \
    file://tftp-mmc-boot.txt \
    file://uEnv.txt \
    "

do_compile[depends] += "opensbi-qilai:do_deploy"

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
