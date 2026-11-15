package utils;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class VideoRecorderUtil {
    private static final ThreadLocal<ScheduledExecutorService> executor = new ThreadLocal<>();
    private static final ThreadLocal<AWTSequenceEncoder> encoder = new ThreadLocal<>();
    private static final ThreadLocal<File> videoFile = new ThreadLocal<>();
    private static final ThreadLocal<AtomicBoolean> isRunning = new ThreadLocal<>();
    private static final ThreadLocal<AtomicInteger> frameCount = new ThreadLocal<>();
    private static final ThreadLocal<WebDriver> activeDriver = new ThreadLocal<>();

    public static void startRecording(WebDriver driver, String scenarioName) {
        if (driver == null) return;
        try {
            File videoDir = new File(System.getProperty("user.dir") + "/target/videos/");
            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }

            String cleanName = scenarioName.replaceAll("[^a-zA-Z0-9_-]", "_");
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File outputFile = new File(videoDir, cleanName + "_" + timestamp + ".mp4");

            // Set to 2 FPS so fast browser actions remain clearly visible during playback
            AWTSequenceEncoder sequenceEncoder = AWTSequenceEncoder.createSequenceEncoder(outputFile, 2);
            AtomicBoolean runningFlag = new AtomicBoolean(true);
            AtomicInteger counter = new AtomicInteger(0);

            videoFile.set(outputFile);
            encoder.set(sequenceEncoder);
            isRunning.set(runningFlag);
            frameCount.set(counter);
            activeDriver.set(driver);

            // Capture an immediate initial frame on the main thread
            captureSingleFrame(driver, sequenceEncoder, counter);

            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            executor.set(service);

            service.scheduleWithFixedDelay(() -> {
                if (!runningFlag.get()) return;
                captureSingleFrame(driver, sequenceEncoder, counter);
            }, 250, 250, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void captureStepFrame() {
        WebDriver driver = activeDriver.get();
        AWTSequenceEncoder sequenceEncoder = encoder.get();
        AtomicInteger counter = frameCount.get();
        if (driver != null && sequenceEncoder != null && counter != null) {
            captureSingleFrame(driver, sequenceEncoder, counter);
        }
    }

    public static File stopRecording(boolean keepVideo) {
        AtomicBoolean runningFlag = isRunning.get();
        if (runningFlag != null) {
            runningFlag.set(false);
        }

        ScheduledExecutorService service = executor.get();
        AWTSequenceEncoder sequenceEncoder = encoder.get();
        File recordedFile = videoFile.get();
        AtomicInteger counter = frameCount.get();
        WebDriver driver = activeDriver.get();

        try {
            if (service != null) {
                service.shutdown();
                service.awaitTermination(5, TimeUnit.SECONDS);
            }

            // Capture one final frame before closing the browser
            if (driver != null && sequenceEncoder != null && counter != null) {
                captureSingleFrame(driver, sequenceEncoder, counter);
            }

            if (sequenceEncoder != null) {
                synchronized (sequenceEncoder) {
                    if (counter != null && counter.get() > 0) {
                        sequenceEncoder.finish();
                    }
                }
            }

            if (recordedFile != null && recordedFile.exists()) {
                if (!keepVideo || (counter != null && counter.get() == 0) || recordedFile.length() == 0) {
                    recordedFile.delete();
                    recordedFile = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.remove();
            encoder.remove();
            videoFile.remove();
            isRunning.remove();
            frameCount.remove();
            activeDriver.remove();
        }
        return recordedFile;
    }

    private static void captureSingleFrame(WebDriver driver, AWTSequenceEncoder sequenceEncoder, AtomicInteger counter) {
        try {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            BufferedImage rawImage = ImageIO.read(new ByteArrayInputStream(screenshotBytes));
            if (rawImage != null) {
                BufferedImage cleanFrame = normalizeImage(rawImage);
                synchronized (sequenceEncoder) {
                    sequenceEncoder.encodeImage(cleanFrame);
                    counter.incrementAndGet();
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static BufferedImage normalizeImage(BufferedImage source) {
        int evenWidth = source.getWidth() % 2 == 0 ? source.getWidth() : source.getWidth() - 1;
        int evenHeight = source.getHeight() % 2 == 0 ? source.getHeight() : source.getHeight() - 1;

        BufferedImage rgbImage = new BufferedImage(evenWidth, evenHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = rgbImage.createGraphics();
        g.drawImage(source, 0, 0, evenWidth, evenHeight, null);
        g.dispose();
        return rgbImage;
    }
}
