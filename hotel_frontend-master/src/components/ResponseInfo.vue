<script setup>
defineProps({
  responseData: Object,
  apiUrl: String,
});
</script>

<template>
  <div class="responseInfo">
    Response from {{ apiUrl }}:
    <pre>
      <code>
        {{ responseData }}
      </code>
    </pre>
  </div>
</template>

<style scoped>
pre {
  background: #f2f2f2;
  color: black;
  margin: 20px;
  padding: 10px;
}

.responseInfo {
  padding: 15px;
}
</style>
