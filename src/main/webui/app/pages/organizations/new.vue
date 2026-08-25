<script setup>
import { postApiV2Issuers } from '~/services/issuer-resource/issuer-resource';

definePageMeta({ title: 'Nueva Organización' })

const toast = useToast()
const submitting = ref(false)

const onSubmitCreateOrganization = (e) => {
    submitting.value = true
    postApiV2Issuers({
        name: e.target.name.value,
        url: e.target.url.value,
        logoUrl: e.target.logoUrl.value,
        email: e.target.email.value,
        description: e.target.description.value,
    })
        .then(_ => e.target.reset())
        .then(_ => toast.success({ title: 'Organización creada' }))
        .then(_ => navigateTo('/organizations'))
        .catch(_ => toast.error({ title: 'Error al crear la organización' }))
        .finally(() => submitting.value = false)
}
</script>

<template>
    <div class="max-w-3xl">
        <NuxtLink to="/organizations" class="link inline-flex items-center gap-1 text-sm mb-6">
            <Icon name="material-symbols:arrow-back" class="text-lg" />
            Volver a organizaciones
        </NuxtLink>

        <div class="page-head">
            <h1 class="page-title">Nueva organización</h1>
            <p class="page-sub">Registra una entidad emisora en la plataforma.</p>
        </div>

        <div class="card card-pad">
            <form @submit.prevent="onSubmitCreateOrganization" class="grid grid-cols-1 md:grid-cols-2 gap-5">
                <div class="field">
                    <label class="field-label" for="name">Organización *</label>
                    <input id="name" type="text" name="name" class="input" required />
                </div>
                <div class="field">
                    <label class="field-label" for="url">Sitio web</label>
                    <input id="url" type="url" name="url" class="input" placeholder="https://..." />
                </div>
                <div class="field">
                    <label class="field-label" for="logoUrl">URL del logo</label>
                    <input id="logoUrl" type="url" name="logoUrl" class="input" placeholder="https://..." />
                </div>
                <div class="field">
                    <label class="field-label" for="email">Email de contacto *</label>
                    <input id="email" type="email" name="email" class="input" required />
                </div>
                <div class="field md:col-span-2">
                    <label class="field-label" for="description">Descripción</label>
                    <textarea id="description" name="description" class="textarea" rows="4"></textarea>
                </div>
                <div class="md:col-span-2 flex justify-end gap-3 pt-2">
                    <NuxtLink to="/organizations" class="btn btn-ghost">Cancelar</NuxtLink>
                    <button class="btn btn-primary" :disabled="submitting">
                        <span v-if="submitting" class="spinner"></span>
                        <template v-else>
                            Crear organización
                            <Icon name="material-symbols:arrow-forward" class="text-lg" />
                        </template>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
