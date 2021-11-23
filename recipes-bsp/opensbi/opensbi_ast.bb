SUMMARY = "RISC-V Open Source Supervisor Binary Interface (OpenSBI)"
DESCRIPTION = "OpenSBI aims to provide an open-source and extensible implementation of the RISC-V SBI specification for a platform specific firmware (M-mode) and a general purpose OS, hypervisor or bootloader (S-mode or HS-mode). OpenSBI implementation can be easily extended by RISC-V platform or System-on-Chip vendors to fit a particular hadware configuration."
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD;md5=42dd9555eb177f35150cf9aa240b61e5"

inherit autotools-brokensep deploy

PV = "0.7+git${SRCPV}"
BRANCH = "opensbi-ast-v5_0_0-branch"

SRC_URI = "git://${LOCAL_SRC}/opensbi;protocol=file;branch=${BRANCH} \
           file://0001-Add-fno-stack-protector-to-avoid-linking-errors.patch \
           file://0002-Add-fno-pie-no-pie-to-disable-PIC.patch \
           file://0003-Comment-out-cache-hook.patch \
           file://0004-Fix-gcc-11-compiling-errors.patch \
           file://0005-Add-custom-csr.patch \
          "

SRCREV = "57e024a403b3e88c7b8181d9dccc4a9b5d379a89"
S = "${WORKDIR}/git"

EXTRA_OEMAKE += "PLATFORM=${RISCV_SBI_PLAT} I=${D} INSTALL_LIB_PATH=lib FW_PIC=n"

do_install:append() {
	# In the future these might be required as a dependency for other packages.
	# At the moment just delete them to avoid warnings
	find ${D}
	rm -r ${D}/include
	rm -r ${D}/lib
	rm -r ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/payloads
}

do_deploy () {
	install -m 755 ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_payload.* ${DEPLOYDIR}/
	install -m 755 ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_jump.* ${DEPLOYDIR}/
	install -m 755 ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_dynamic.* ${DEPLOYDIR}/
}

addtask deploy before do_build after do_install

FILES:${PN} += "/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_jump.*"
FILES:${PN} += "/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_payload.*"
FILES:${PN} += "/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_dynamic.*"

COMPATIBLE_HOST = "(riscv64|riscv32).*"
INHIBIT_PACKAGE_STRIP = "1"

SECURITY_CFLAGS = ""
