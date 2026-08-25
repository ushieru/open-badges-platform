<script setup>
const route = useRoute()

onMounted(() => {
    return navigateTo(`/api/auth/logout?r=` + route.query.r, { external: true })
})
</script>

<template>
    <div class="grid min-h-[70vh] place-items-center">
        <div class="flex flex-col items-center gap-5">
            <AppLogo :size="56" />
            <div class="flex items-center gap-3 text-ink-soft">
                <span class="spinner"></span>
                <span class="text-sm">Cerrando sesión...</span>
            </div>
        </div>
    </div>
</template>
