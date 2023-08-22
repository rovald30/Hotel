<script setup>
import WelcomeItem from "./WelcomeItem.vue";
import DocumentationIcon from "./icons/IconDocumentation.vue";
import IconTooling from "@/components/icons/IconTooling.vue";
import CounterWithPinia from "@/components/CounterWithPinia.vue";
import FieldText from "@/components/_atoms/FieldText/FieldText.vue";
import DataMainView from "@/views/DataView/DataMainView.vue";
</script>

<template>
  <DataMainView />
  <div class="body">
    <WelcomeItem>
      <template #icon>
        <IconTooling />
      </template>
      <template #heading>Counter with Pinia</template>

      <CounterWithPinia />
    </WelcomeItem>

    <WelcomeItem>
      <template #icon>
        <IconTooling />
      </template>
      <template #heading>FieldText with Pinia</template>

      <FieldText />
    </WelcomeItem>

    <WelcomeItem>
      <template #icon>
        <DocumentationIcon />
      </template>
      <template #heading>Documentation</template>

      Vue’s
      <a href="https://vuejs.org/" target="_blank" rel="noopener"
        >official documentation</a
      >
      provides you with all information you need to get started.
    </WelcomeItem>
  </div>
</template>

<style scoped>
.body {
  padding-top: 3rem;
}
</style>
