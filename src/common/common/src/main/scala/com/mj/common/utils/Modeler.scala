package com.mj.common.utils

import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.sql.DataFrame

object Modeler {
  def apply(clusters: Int,
            iterators: Int,
            seed: Long,
            df: DataFrame): KMeansModel = {
    // Trains a k-means model.
    val kmeans = new KMeans().setK(clusters).setMaxIter(iterators).setSeed(seed)
    kmeans.fit(df)
  }
}
