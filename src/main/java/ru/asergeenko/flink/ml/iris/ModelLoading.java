package ru.asergeenko.flink.ml.iris;

import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import com.alibaba.alink.pipeline.Pipeline;
import com.alibaba.alink.pipeline.PipelineModel;
import com.alibaba.alink.pipeline.classification.LogisticRegression;
import com.alibaba.alink.pipeline.dataproc.Imputer;
import com.alibaba.alink.pipeline.nlp.DocCountVectorizer;
import com.alibaba.alink.pipeline.nlp.Segment;
import com.alibaba.alink.pipeline.nlp.StopWordsRemover;
import org.apache.flink.api.java.ExecutionEnvironment;

public class ModelLoading {
    public static void main(String[] args) throws Exception {

        CsvSourceBatchOp validationSet = new CsvSourceBatchOp()
                .setFilePath("src/main/resources/valid.csv")
                .setSchemaStr("text string, label int")
                .setIgnoreFirstLine(true);

        PipelineModel modelDes = PipelineModel.load("src/main/resources/model");
        modelDes.transform(validationSet.firstN(100))
                .select(new String[]{"text", "label", "pred"})
                .firstN(5)
                .print();

    }
}
