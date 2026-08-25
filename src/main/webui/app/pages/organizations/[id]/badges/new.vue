<script setup>
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import { postApiV2IssuersIssuerUuidBadges } from '~/services/issuer-badge-class-resource/issuer-badge-class-resource'

const toast = useToast()
const route = useRoute()
const organizationId = route.params.id

const criteria = ref('')
const imageb64 = ref('')
const tab = ref('edit')
const submitting = ref(false)

const sanitizedHtml = computed(() => {
    const rawHtml = marked.parse(criteria.value || '')
    return DOMPurify.sanitize(rawHtml)
})

const handleImageUpdate = (base64) => imageb64.value = base64

const onSubmitCreateBadge = (e) => {
    submitting.value = true
    postApiV2IssuersIssuerUuidBadges(organizationId, {
        name: e.target.badge.value,
        description: e.target.description.value,
        criteriaMd: criteria.value,
        imageBase64: imageb64.value
    })
        .then(data => data.status != 200 ? Promise.reject() : Promise.resolve())
        .then(_ => e.target.reset())
        .then(_ => toast.success({ title: 'Credencial creada' }))
        .then(_ => navigateTo(`/organizations/${organizationId}`))
        .catch(_ => toast.error({ title: 'Error al crear la credencial' }))
        .finally(() => submitting.value = false)
}
</script>

<template>
    <div class="max-w-3xl">
        <NuxtLink :to="`/organizations/${organizationId}`" class="link inline-flex items-center gap-1 text-sm mb-6">
            <Icon name="material-symbols:arrow-back" class="text-lg" />
            Volver a la organización
        </NuxtLink>

        <div class="page-head">
            <h1 class="page-title">Nueva credencial</h1>
            <p class="page-sub">Crea una insignia digital para tu organización.</p>
        </div>

        <div class="card card-pad">
            <form @submit.prevent="onSubmitCreateBadge" class="flex flex-col gap-6">
                <div class="flex flex-col items-center">
                    <OrganizationsBadgeImageUploader @update:image="handleImageUpdate" />
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-5">
                    <div class="field md:col-span-2">
                        <label class="field-label" for="badge">Nombre de la credencial *</label>
                        <input id="badge" type="text" name="badge" class="input" required />
                    </div>
                    <div class="field md:col-span-2">
                        <label class="field-label" for="description">Descripción</label>
                        <textarea id="description" name="description" class="textarea" rows="4"></textarea>
                    </div>
                </div>

                <div class="field">
                    <span class="field-label">Criterios (Markdown)</span>
                    <div class="flex rounded-xl border border-line-strong bg-surface-2 p-1">
                        <button type="button" class="flex-1 rounded-lg px-4 py-2 text-sm font-semibold transition-colors"
                            :class="tab === 'edit' ? 'bg-surface text-ink shadow-sm' : 'text-ink-soft hover:text-ink'" @click="tab = 'edit'">
                            Editar
                        </button>
                        <button type="button" class="flex-1 rounded-lg px-4 py-2 text-sm font-semibold transition-colors"
                            :class="tab === 'preview' ? 'bg-surface text-ink shadow-sm' : 'text-ink-soft hover:text-ink'" @click="tab = 'preview'">
                            Previsualizar
                        </button>
                    </div>
                    <textarea v-if="tab === 'edit'" v-model="criteria" name="criteria" class="textarea font-mono" rows="12" placeholder="# Criterios&#10;Describe aquí qué se necesita para obtener esta credencial..."></textarea>
                    <div v-else class="rounded-xl border border-line bg-surface-2 p-5">
                        <div class="prose prose-sm max-w-none dark:prose-invert" v-html="sanitizedHtml || '<p class=\'text-ink-soft\'>Aún no hay criterios escritos.</p>'"></div>
                    </div>
                </div>

                <div class="flex justify-end gap-3 pt-2">
                    <NuxtLink :to="`/organizations/${organizationId}`" class="btn btn-ghost">Cancelar</NuxtLink>
                    <button class="btn btn-primary" :disabled="submitting">
                        <span v-if="submitting" class="spinner"></span>
                        <template v-else>
                            Crear credencial
                            <Icon name="material-symbols:arrow-forward" class="text-lg" />
                        </template>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
