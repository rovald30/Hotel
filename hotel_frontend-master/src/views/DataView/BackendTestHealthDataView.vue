<script setup>
import ResponseInfo from "@/components/ResponseInfo.vue";
import { useBackendTestHealthStore } from "@/stores/backendTestHealth";
import { storeToRefs } from "pinia";
import DataMainView from "@/views/DataView/DataMainView.vue";

const { responseData, apiUrl } = storeToRefs(useBackendTestHealthStore());
</script>

<template>
  <DataMainView />
  <ResponseInfo :response-data="responseData" :api-url="apiUrl" />
</template>
