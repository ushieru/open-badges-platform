<script setup>
const route = useRoute()
const config = useRuntimeConfig()

onMounted(() => {
    return navigateTo(`${config.public.siteUrl}/api/auth/logout?r=` + route.query.r, { external: true })
})
</script>

<template>
    <div class="grid place-content-center h-screen">
        <span class="loading loading-ring loading-xl"></span>
    </div>
</template>