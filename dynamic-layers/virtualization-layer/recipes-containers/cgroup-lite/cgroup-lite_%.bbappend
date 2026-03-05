inherit finit

FINIT_SERVICES:${PN} = "cgroups-mount"

do_install:append() {
    sed -i -e "s#systemd#${INIT_MANAGER}#g" ${D}${base_bindir}/cgroups-mount
}
