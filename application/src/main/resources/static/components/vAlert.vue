<template>
    <div class="alert_main" v-show="isShow">
        <div class="alert">
            <div class="alert_cont">
                <p v-html="content"></p>
                <div class="btn_wrap">
                    <input type="button" class="btnNavy yes btnLarge" ref="alertOk" :value="okLabel" @click.prevent="onComplete()"/>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    props: {
        isShow: {
            type: Boolean,
            default: false
        },
        content: {
            type: String,
            default: "확인해주세요"
        }
    },
    data: function() { return {
        okLabel: '확인'
    }},
    mounted: function() {
        this.$refs.alertOk.focus();
    },
    methods: {
        onComplete(flag) {
            this.isShow = !this.isShow;
            this.$emit("complete");
        }
    }
}
</script>

<style lang="css" scoped>
.alert_main {
    display: block;
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    text-align: center;
    background: rgba(0, 0, 0, 0.5);
    z-index: 999;
}
.alert {
    position: relative;
    width: 600px;
    min-height: 200px;
    background: #fff;
    border: 1px solid #2d358d;
    border-radius: 5px;
    overflow: hidden;
    box-shadow: 0 4px 6px 0 hsla(0, 0%, 0%, 0.3);
    margin: 30vh auto;
}
.alert_cont {
    width: 100%;
    height: auto;
    padding: 20px;
    text-align: center;
    font-size: 16px;
}
</style>