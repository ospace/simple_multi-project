<template>
    <ul class="pagination justify-content-center">
        <template v-if="beginPage == 1">
            <li class='page-item disabled'>
                <a class='page-link' href='#'>First</a>
            </li>
            <li class='page-item disabled'>
                <a class='page-link' href='#'>Prev</a>
            </li>
        </template>
        <template v-else>
            <li class='page-item'>
                <a class="page-link" href='#'
                    @click.prevent="$emit('change', toOffset(1))">First</a>
            </li>
            <li class='page-item'>
                <a class="page-link" href='#'
                    @click.prevent="$emit('change', toOffset(beginPage-1))">Prev</a>
            </li>
        </template>
        
        <div class="page" v-for="index in pageRange" :key="index">
            <li class='page-item active' v-if='pageNo == index'>
                <a href='#' class='page-link'>{{index}}</a>
            </li>
            <li class='page-item' v-else :key='index'>
                <a href='#' class='page-link'
                    @click.prevent='$emit("change", toOffset(index))'>{{index}}</a>
            </li>
        </div>

        <template v-if='endPage == totalPage'>
            <li class='page-item disabled'>
                <a class='page-link' href='#'>Next</a>
            </li>
            <li class='page-item disabled'>
                <a class='page-link' href='#'>Last</a>
            </li>
        </template>
        <template v-else>
            <li class='page-item'>
                <a class='page-link' href='#' @click.prevent='$emit("change", toOffset(endPage+1))'>Next</a>
            </li>
            <li class='page-item'>
                <a class='page-link' href='#' @click.prevent='$emit("change", toOffset(totalPage))'>Last</a>
            </li>
        </template>
    </div>
</template>

<script>
module.exports = {
    props: {
        offset: {
            type: Number,
            default: 0
        },
        total: {
            type: Number,
            default: 0
        },
        limit: {
            type: Number,
            default: 10
        },
        size: {
            type: Number,
            default: 10
        }
    },
    computed: {
        beginPage: function() {
            return this.pageIndex * this.size + 1;
        },
        endPage: function() {
            return Math.min((this.pageIndex+1)*this.size, this.totalPage);
        },
        pageIndex: function() {
            return parseInt((this.pageNo-1)/this.size);
        },
        totalPage: function() {
            return Math.ceil(this.total/this.limit);
        },
        pageNo: function() {
            return this.offset/this.limit+1;
        },
        pageRange: function() {
            let ret = [];
            for(let i=this.beginPage; i<=this.endPage; ++i) {
                ret.push(i);
            }
            return ret;
        }
    },
    methods: {
        toOffset: function (pageNo) {
            return this.limit * (pageNo-1);
        }
    }
}
</script>