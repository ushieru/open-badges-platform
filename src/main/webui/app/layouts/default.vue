<script setup>
const { me, meResponse } = useAuth()
const { isDark, init, toggle } = useTheme()
const mobileOpen = ref(false)

onMounted(() => init())
onBeforeUnmount(() => { mobileOpen.value = false })

const navLinks = computed(() => {
    const links = []
    if (me.value) {
        links.push({ to: '/badges', label: 'Mis credenciales', icon: 'material-symbols:workspace-premium' })
        if (me.value.memberships?.length > 0)
            links.push({ to: '/hubs', label: 'Mis organizaciones', icon: 'material-symbols:hub' })
        if (me.value.account?.isSuperAdmin)
            links.push({ to: '/organizations', label: 'Organizaciones', icon: 'material-symbols:domain' })
    }
    return links
})

const initials = computed(() => {
    const name = me.value?.account?.fullName || ''
    return name.split(' ').filter(Boolean).slice(0, 2).map(p => p[0]).join('').toUpperCase()
})
</script>

<template>
    <div class="min-h-[100dvh] flex flex-col">
        <!-- Skip link -->
        <a href="#main" class="sr-only focus:not-sr-only focus:fixed focus:top-4 focus:left-4 focus:z-[60] focus:px-4 focus:py-2 focus:rounded-lg focus:bg-teal focus:text-white focus:font-semibold">
            Saltar al contenido
        </a>

        <!-- Topbar -->
        <header class="sticky top-0 z-40 border-b border-line bg-surface/80 backdrop-blur-xl">
            <div class="mx-auto flex h-16 max-w-7xl items-center justify-between gap-4 px-4 sm:px-6">
                <div class="flex items-center gap-6">
                    <NuxtLink to="/" aria-label="Ir al inicio">
                        <AppLogo with-wordmark />
                    </NuxtLink>
                    <nav class="hidden lg:flex items-center gap-1" aria-label="Principal">
                        <NuxtLink v-for="link in navLinks" :key="link.to" :to="link.to"
                            class="px-3 py-2 text-sm font-medium text-ink-soft rounded-lg hover:text-teal hover:bg-teal-soft transition-colors">
                            {{ link.label }}
                        </NuxtLink>
                    </nav>
                </div>

                <div class="flex items-center gap-2">
                    <!-- Theme toggle -->
                    <button class="btn btn-ghost btn-icon" :aria-label="isDark ? 'Activar modo claro' : 'Activar modo oscuro'"
                        @click="toggle">
                        <Icon v-if="isDark" name="material-symbols:light-mode-rounded" class="text-xl" />
                        <Icon v-else name="material-symbols:dark-mode-rounded" class="text-xl" />
                    </button>

                    <!-- Mobile menu toggle -->
                    <button class="btn btn-ghost btn-icon lg:hidden" aria-label="Abrir menú" :aria-expanded="mobileOpen"
                        @click="mobileOpen = !mobileOpen">
                        <div class="relative w-5 h-4">
                            <span class="absolute left-0 top-0 h-0.5 w-full bg-ink transition-all duration-300"
                                :class="mobileOpen ? 'top-1/2 -translate-y-1/2 rotate-45' : ''"></span>
                            <span class="absolute left-0 top-1/2 -translate-y-1/2 h-0.5 w-full bg-ink transition-all duration-300"
                                :class="mobileOpen ? 'opacity-0' : ''"></span>
                            <span class="absolute left-0 bottom-0 h-0.5 w-full bg-ink transition-all duration-300"
                                :class="mobileOpen ? 'bottom-1/2 translate-y-1/2 -rotate-45' : ''"></span>
                        </div>
                    </button>

                    <template v-if="me == null">
                        <NuxtLink v-if="meResponse?.status == 404" to="/logout?r=login" class="btn btn-outline btn-sm">
                            Usar otra cuenta
                        </NuxtLink>
                        <NuxtLink v-else :to="'/login?r=' + $route.path" class="btn btn-primary btn-sm">
                            Iniciar sesión
                        </NuxtLink>
                    </template>
                    <div v-else class="relative group">
                        <button class="btn btn-ghost !p-1.5 rounded-full" aria-haspopup="menu" aria-label="Menú de usuario">
                            <span class="flex h-9 w-9 items-center justify-center rounded-full bg-gradient-to-br from-[#0e7490] to-[#0b1b33] text-sm font-bold text-white">
                                {{ initials || '?' }}
                            </span>
                        </button>
                        <div class="invisible opacity-0 translate-y-2 group-hover:visible group-hover:opacity-100 group-hover:translate-y-0
                            absolute right-0 top-full mt-2 w-60 rounded-2xl border border-line bg-surface p-2 shadow-card-hover
                            transition-all duration-300 origin-top-right" role="menu">
                            <div class="px-3 py-2 border-b border-line mb-1">
                                <p class="text-sm font-semibold truncate">{{ me?.account?.fullName }}</p>
                                <p class="text-xs text-ink-soft truncate">{{ me?.account?.email }}</p>
                            </div>
                            <NuxtLink to="/profile" class="flex items-center gap-2 rounded-lg px-3 py-2 text-sm text-ink-soft hover:text-teal hover:bg-teal-soft transition-colors" role="menuitem">
                                <Icon name="material-symbols:person" class="text-lg" />
                                Perfil
                            </NuxtLink>
                            <NuxtLink to="/badges" class="flex items-center gap-2 rounded-lg px-3 py-2 text-sm text-ink-soft hover:text-teal hover:bg-teal-soft transition-colors" role="menuitem">
                                <Icon name="material-symbols:workspace-premium" class="text-lg" />
                                Mis credenciales
                            </NuxtLink>
                            <NuxtLink to="/logout?r=" class="flex items-center gap-2 rounded-lg px-3 py-2 text-sm text-danger hover:bg-danger/10 transition-colors" role="menuitem">
                                <Icon name="material-symbols:logout" class="text-lg" />
                                Cerrar sesión
                            </NuxtLink>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Mobile menu -->
            <div v-if="mobileOpen" class="lg:hidden border-t border-line bg-surface">
                <nav class="mx-auto max-w-7xl px-4 py-3 flex flex-col gap-1" aria-label="Principal móvil">
                    <NuxtLink v-for="link in navLinks" :key="link.to" :to="link.to" @click="mobileOpen = false"
                        class="flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium text-ink-soft hover:text-teal hover:bg-teal-soft transition-colors">
                        <Icon :name="link.icon" class="text-xl" />
                        {{ link.label }}
                    </NuxtLink>
                    <NuxtLink v-if="me" to="/profile" @click="mobileOpen = false"
                        class="flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium text-ink-soft hover:text-teal hover:bg-teal-soft transition-colors">
                        <Icon name="material-symbols:person" class="text-xl" />
                        Perfil
                    </NuxtLink>
                </nav>
            </div>
        </header>

        <!-- Main -->
        <main id="main" class="flex-1 w-full">
            <div class="mx-auto max-w-7xl px-4 sm:px-6 py-10 md:py-14">
                <slot></slot>
            </div>
        </main>

        <!-- Footer -->
        <footer class="border-t border-line bg-surface-2">
            <div class="mx-auto max-w-7xl px-4 sm:px-6 py-12">
                <div class="flex flex-col md:flex-row items-center justify-between gap-8">
                    <div class="flex flex-col items-center md:items-start gap-3">
                        <AppLogo with-wordmark :size="32" />
                        <p class="text-sm text-ink-soft text-center md:text-left max-w-sm">
                            Reconocimiento soberano, inmutable y conforme a estándares.
                        </p>
                    </div>
                    <nav class="flex flex-wrap items-center justify-center gap-x-6 gap-y-2" aria-label="Legal">
                        <NuxtLink to="/terms" class="text-sm text-ink-soft hover:text-teal transition-colors">Términos de uso</NuxtLink>
                        <NuxtLink to="/privacy" class="text-sm text-ink-soft hover:text-teal transition-colors">Aviso de privacidad</NuxtLink>
                        <a href="https://github.com/gdgguadalajara/open-badges-platform" target="_blank" rel="noopener"
                            class="text-sm text-ink-soft hover:text-teal transition-colors">GitHub</a>
                    </nav>
                </div>
                <div class="mt-8 pt-6 border-t border-line flex flex-col sm:flex-row items-center justify-between gap-3">
                    <p class="text-xs text-ink-soft">Hecho con <span aria-hidden="true">💙</span> por la comunidad de GDG Guadalajara</p>
                    <NuxtLink to="https://gdg.community.dev/gdg-guadalajara/" target="_blank" rel="noopener"
                        class="text-xs text-ink-soft hover:text-teal transition-colors">gdg.community.dev/gdg-guadalajara</NuxtLink>
                </div>
            </div>
        </footer>
    </div>
</template>
