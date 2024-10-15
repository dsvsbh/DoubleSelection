<template>
    <div class="app-container">
        <el-card v-if="resultList[0].name" class="res_content">
            <div class="block">
                <el-timeline>
                    <el-timeline-item :timestamp="value.time" placement="top" v-for="(value, index) in resultList"
                        :key="index" :color="value.color">
                        <el-card class="res_content">
                            <h1 v-if="value.name">{{ value.name }}</h1>
                            <h2 v-if="value.type == 1">活动开启</h2>
                            <h2 v-else-if="value.type == 0">活动结束</h2>
                            <h3 v-if="value.description">活动详情：<br>{{ value.description }}</h3>

                        </el-card>
                    </el-timeline-item>
                </el-timeline>
            </div>
        </el-card>

        <el-empty v-show="resultList[0].name === ''" description="暂无活动"></el-empty>

    </div>
</template>

<script>
import { getCurrentActivity } from '../../../api/doubleSelection/currentActivity';

export default {
    data() {
        return {
            // 1 表示活动开启 0 表示活动结束
            // 2 表示研究生已选择成功等待导师 3 表示导师已选择该研究生 4 表示导师未选择该研究生
            resultList: [
                {
                    time: '',
                    name: "",
                    description: '',
                    type: '1'
                },
                // {
                //     time: '2021/7/13',
                //     content: '测试员 已选择导师 导师1号',
                //     type: '2'
                // },
                // {
                //     time: '2021/7/14',
                //     content: '导师1号 已选择研究生 测试员',
                //     type: '3'
                // },
                // {
                //     time: '2021/7/15',
                //     content: '导师1号 未选择 测试员',
                //     type: '4'
                // },
                {
                    time: '',
                    name: "",
                    description: '',
                    type: '0'
                }
            ]
        };
    },
    created() {
        // console.log(getCurrentActivity().data);
        this.handleCurrent()
        this.setColor()
    },
    mounted() {

    },
    methods: {
        handleCurrent() {
            getCurrentActivity().then(response => {
                if (response.data == null) {
                    this.resultList = []
                    this.$modal.msgSuccess("当前无活动");
                } else {
                    const data = response.data
                    this.resultList[0].name = data.name
                    this.resultList[1].name = data.name
                    this.resultList[0].time = data.createTime
                    this.resultList[1].time = data.endTime
                    this.resultList[0].description = data.description
                    this.resultList[1].description = data.description
                }

            })
        },

        setColor() {
            this.resultList.forEach((item) => {
                if (item.type == 1 || item.type == 0) item.color = '#4876FF'
                else if (item.type == 2) item.color = '#EEAD0E'
                else if (item.type == 3) item.color = '#0bbd87'
                else if (item.type == 4) item.color = '#EE2C2C'
            })
        }
    }
};
</script>

<style scoped lang="scss">
.res_content {
    max-height: calc(80vh);
    overflow: auto;
}

.block {
    .el-card__body {
        h4 {
            font-size: 16px;
        }

        p {
            // padding-top: 8px;
            font-size: 15px;
        }
    }
}
</style>
