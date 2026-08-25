<script setup>
import { postApiAuthRegister } from '~/services/authentication-resource/authentication-resource';

const toast = useToast()
const submitting = ref(false)

const onCreateAccountSubmit = (e) => {
    submitting.value = true
    postApiAuthRegister({ name: e.target.name.value, acceptedLegal: e.target.legal.checked })
        .then(({ data, status }) => status == 200 ? Promise.resolve(data) : Promise.reject(data))
        .then(() => navigateTo('/', { external: true }))
        .catch((data) => toast.error({ message: data.message }))
        .finally(() => submitting.value = false)
}
</script>

<template>
    <div class="grid min-h-[70vh] place-items-center">
        <div class="w-full max-w-lg">
            <div class="flex flex-col items-center text-center mb-8">
                <AppLogo :size="64" class="mb-4" />
                <h1 class="font-display text-3xl font-bold tracking-tight">Crea tu cuenta</h1>
                <p class="mt-2 text-ink-soft">Comienza a gestionar tus credenciales digitales.</p>
            </div>
            <div class="card card-pad">
                <form @submit.prevent="onCreateAccountSubmit" class="flex flex-col gap-5">
                    <div class="field">
                        <label class="field-label" for="name">Nombre completo *</label>
                        <input id="name" type="text" name="name" class="input" required />
                    </div>
                    <label class="flex items-start gap-3 rounded-xl border border-line bg-surface-2 p-4 cursor-pointer">
                        <input type="checkbox" name="legal" class="mt-0.5 h-4 w-4 accent-[var(--teal)]" />
                        <span class="text-sm text-ink-soft">
                            He leído la <NuxtLink to="/privacy" target="_blank" class="link">política de privacidad</NuxtLink>
                            y acepto los <NuxtLink to="/terms" target="_blank" class="link">términos y condiciones</NuxtLink>.
                        </span>
                    </label>
                    <button class="btn btn-primary btn-block btn-lg" :disabled="submitting">
                        <span v-if="submitting" class="spinner"></span>
                        <template v-else>
                            Crear cuenta
                            <Icon name="material-symbols:arrow-forward" class="text-lg" />
                        </template>
                    </button>
                </form>
            </div>
        </div>
    </div>
</template>
