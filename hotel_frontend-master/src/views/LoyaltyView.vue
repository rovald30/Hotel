<script setup>
import Navbar from "@/components/_organisms/Navbar/Navbar.vue";
import ContentWrapper from "@/components/_molecules/ContentWrapper/ContentWrapper.vue";
import LoremIpsum from "@/components/_atoms/LoremIpsum/LoremIpsum.vue";
</script>

<template>
  <Navbar />
  <ContentWrapper>
    <p>Loyalty</p>
    <LoremIpsum />
  </ContentWrapper>
</template>

<style scoped>
p {
  padding: 50px 0 50px 0;
}
</style>
