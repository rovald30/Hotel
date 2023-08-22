<script setup>
import ResponseInfo from "@/components/ResponseInfo.vue";
import { useRoomsStore } from "@/stores/rooms";
import { storeToRefs } from "pinia";
import DataMainView from "@/views/DataView/DataMainView.vue";

const { responseData, apiUrl } = storeToRefs(useRoomsStore());
</script>

<template>
  <DataMainView />
  <ResponseInfo :response-data="responseData" :api-url="apiUrl" />
</template>
