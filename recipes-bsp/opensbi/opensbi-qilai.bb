SUMMARY = "RISC-V Open Source Supervisor Binary Interface (OpenSBI)"
DESCRIPTION = "OpenSBI aims to provide an open-source and extensible implementation of the RISC-V SBI specification for a platform specific firmware (M-mode) and a general purpose OS, hypervisor or bootloader (S-mode or HS-mode). OpenSBI implementation can be easily extended by RISC-V platform or System-on-Chip vendors to fit a particular hadware configuration."
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD;md5=42dd9555eb177f35150cf9aa240b61e5"
FILESEXTRAPATHS:prepend := "${THISDIR}/qilai:"

inherit autotools-brokensep deploy

PV = "1.2+git${SRCPV}"

FORK = "andestech"
BRANCH = "ast-v5_3_0-branch"
SRCREV = "6005a1b252ac0d7aa4f8d9cfd4d35b076b9dad8c"
SRC_URI = "git://github.com/${FORK}/opensbi.git;protocol=https;branch=${BRANCH} \
           file://0001-andes-sleep.S-fix-system-hang-caused-by-hotplugging-.patch \
           file://0002-platform-generic-andes-Add-the-print_detailed_cause-.patch \
           file://0003-platform-generic-andes-Fix-an-error-for-print_detail.patch \
           file://0004-riscv-andes-Support-Qilai.patch \
           file://0005-riscv-qilai-setup-PMA-region-as-device-non-bufferabl.patch \
           file://0006-smu-qilai-modify-flash-base-fix-reboot-failed.patch \
           file://0007-Makefile-Force-GNU-hashing.patch \
          "

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "PLATFORM=${RISCV_SBI_PLAT} I=${D} INSTALL_LIB_PATH=lib"

do_install:append() {
	# In the future these might be required as a dependency for other packages.
	# At the moment just delete them to avoid warnings
	find ${D}
	rm -r ${D}/include
	rm -r ${D}/lib
}

do_deploy () {
	install -m 755 ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_dynamic.* ${DEPLOYDIR}/
}

addtask deploy before do_build after do_install

FILES:${PN} += "/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_dynamic.*"
