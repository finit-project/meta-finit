addhandler finit_distrocheck
finit_distrocheck[eventmask] = "bb.event.SanityCheck"
python finit_distrocheck() {
    skip_check = e.data.getVar('SKIP_META_FINIT_SANITY_CHECK') == "1"
    if 'finit' not in e.data.getVar('DISTRO_FEATURES').split() and not skip_check:
        bb.warn("You have included the meta-finit layer, but \
'finit' has not been enabled in your DISTRO_FEATURES. Some bbappend files \
and preferred version setting may not take effect. See the meta-finit README \
for details on enabling finit support.")
}
