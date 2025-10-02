import android.content.Context
import ai.onnxruntime.*
import android.util.Log
import com.ignite.smspredictor.R
import com.ignite.smspredictor.TAG
import java.util.regex.Pattern
import kotlin.math.log

class predictSms(context: Context) {

    private val env: OrtEnvironment = OrtEnvironment.getEnvironment()
    private val session: OrtSession

    init {
        // Load the ORT model from assets
        val inputStream = context.resources.openRawResource(R.raw.sms_model_textonly)
        val modelBytes = inputStream.readBytes()
        session = env.createSession(modelBytes)
    }

    fun predict(text: String): Pair<Boolean, Float> {
        Log.d(TAG, "predict: the entered sms is : $text")
        // Create input tensor (String)
        val inputName = session.inputNames.first()
        val inputTensor = OnnxTensor.createTensor(env, arrayOf(arrayOf(text)))

        // Run inference
        val results = session.run(mapOf(inputName to inputTensor))

        // Extract label (Long) and probabilities (FloatArray)
        val labelTensor = results[0].value as LongArray
        val probsTensor = results[1].value as Array<FloatArray>

        val predictedLabel = labelTensor[0]
        val probabilities = probsTensor[0]

        Log.d(TAG, "predict: $predictedLabel and porb : ${probabilities[1]}")

        return Pair(predictedLabel == 1L,probabilities[1])

//        return Pair(predictedLabel, probabilities)
    }

}