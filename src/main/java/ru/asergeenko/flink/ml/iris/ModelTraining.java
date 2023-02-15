package ru.asergeenko.flink.ml.iris;

import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import com.alibaba.alink.pipeline.Pipeline;
import com.alibaba.alink.pipeline.PipelineModel;
import com.alibaba.alink.pipeline.classification.LogisticRegression;
import com.alibaba.alink.pipeline.dataproc.Imputer;
import com.alibaba.alink.pipeline.nlp.DocCountVectorizer;
import com.alibaba.alink.pipeline.nlp.Segment;
import com.alibaba.alink.pipeline.nlp.StopWordsRemover;

public class ModelTraining {
    public static void main(String[] args) throws Exception {

        CsvSourceBatchOp trainingSet = new CsvSourceBatchOp()
                .setFilePath("src/main/resources/train.csv")
                .setSchemaStr("text string, label int")
                .setIgnoreFirstLine(true);

        CsvSourceBatchOp validationSet = new CsvSourceBatchOp()
                .setFilePath("src/main/resources/valid.csv")
                .setSchemaStr("text string, label int")
                .setIgnoreFirstLine(true);

        trainingSet.firstN(5).print();

        Pipeline pipeline = new Pipeline(
                new Imputer()
                        .setSelectedCols("text")
                        .setOutputCols("featureText")
                        .setStrategy("value")
                        .setFillValue("null"),
                new Segment()
                        .setSelectedCol("featureText"),
                new StopWordsRemover()
                        .setSelectedCol("featureText"),
                new DocCountVectorizer()
                        .setFeatureType("TF")
                        .setSelectedCol("featureText")
                        .setOutputCol("featureVector"),
                new LogisticRegression()
                        .setVectorCol("featureVector")
                        .setLabelCol("label")
                        .setPredictionCol("pred")
        );

        PipelineModel model = pipeline.fit(trainingSet);
        model.save("src/main/resources/model");

        model.transform(validationSet.firstN(100))
                .select(new String[]{"text", "label", "pred"})
                .firstN(5)
                .print();

    }
}
