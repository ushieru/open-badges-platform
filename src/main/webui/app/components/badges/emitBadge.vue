<script setup>
import { postApiV2IssuersIssuerUuidBadgesBadgeClassUuidAssertions } from '~/services/issuer-assertion-resource/issuer-assertion-resource';

const { issuerId, badgeId } = defineProps(['issuerId', 'badgeId'])

const toast = useToast()
const isLoading = ref(false)
const dialogOpen = ref(false)

const emitBadgesSubmit = (e) => {
    if (isLoading.value) return
    isLoading.value = true
    postApiV2IssuersIssuerUuidBadgesBadgeClassUuidAssertions(issuerId, badgeId, {
        emails: e.target.emails.value.split(',').map(email => email.trim()),
        evidenceUrl: e.target.evidence.value,
    })
        .then(data => data.status == 200 ? Promise.resolve() : Promise.reject())
        .then(_ => e.target.reset())
        .then(_ => toast.success({ title: 'Insignias emitidas correctamente' }))
        .then(_ => { dialogOpen.value = false })
        .catch(_ => toast.error({ title: 'Error al emitir las insignias' }))
        .finally(() => isLoading.value = false)
}
</script>

<template>
    <button class="btn btn-gold" @click="dialogOpen = true">
        <Icon name="material-symbols:add-task" class="text-lg" />
        Emitir insignia
    </button>

    <Teleport to="body">
        <div v-if="dialogOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60" @click.self="dialogOpen = false">
            <div class="w-full max-w-lg rounded-3xl border border-line bg-surface p-6 shadow-card-hover" role="dialog" aria-modal="true" aria-labelledby="emit-title">
                <div class="flex items-center justify-between mb-5">
                    <h3 id="emit-title" class="font-display text-lg font-bold">Emitir insignias</h3>
                    <button class="btn btn-ghost btn-icon" aria-label="Cerrar" @click="dialogOpen = false">
                        <Icon name="material-symbols:close" class="text-xl" />
                    </button>
                </div>
                <form @submit.prevent="emitBadgesSubmit" class="flex flex-col gap-5">
                    <div class="field">
                        <label class="field-label" for="emails">Emails</label>
                        <textarea id="emails" name="emails" rows="3" class="textarea" placeholder="usuario1@correo.com, usuario2@correo.com"></textarea>
                        <p class="text-xs text-ink-soft">Separa los correos con comas.</p>
                    </div>
                    <div class="field">
                        <label class="field-label" for="evidence">Evidencia (URL)</label>
                        <input id="evidence" type="url" name="evidence" class="input" placeholder="https://..." />
                    </div>
                    <button class="btn btn-primary btn-block" :disabled="isLoading">
                        <span v-if="isLoading" class="spinner"></span>
                        <template v-else>
                            Emitir insignias
                            <Icon name="material-symbols:arrow-forward" class="text-lg" />
                        </template>
                    </button>
                </form>
            </div>
        </div>
    </Teleport>
</template>
