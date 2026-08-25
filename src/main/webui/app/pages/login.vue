<script setup>
const { meResponse, fetchMe } = useAuth()

onMounted(() => {
    fetchMe()
})

watchEffect(() => {
    if (meResponse.value?.status && meResponse.value?.status >= 300 && meResponse.value?.status <= 399)
        return navigateTo(`/api/auth/login`, { external: true })
    if (meResponse.value?.status && meResponse.value?.status >= 400 && meResponse.value?.status <= 499)
        return navigateTo('/register')
})
</script>

<template>
    <div class="grid min-h-[70vh] place-items-center">
        <div class="flex flex-col items-center gap-5">
            <AppLogo :size="56" />
            <div class="flex items-center gap-3 text-ink-soft">
                <span class="spinner"></span>
                <span class="text-sm">Redirigiendo...</span>
            </div>
        </div>
    </div>
</template>
