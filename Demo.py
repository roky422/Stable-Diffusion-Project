import model_loader
import pipeline
from PIL import Image
from pathlib import Path
from transformers import CLIPTokenizer, BlipProcessor, BlipForConditionalGeneration
from diffusers import AutoPipelineForImage2Image
import torch
import accelerate


def generate_image(prompt, weights_path):
    # Set the device to use for inference
    DEVICE = "cpu"

    ALLOW_CUDA = True
    ALLOW_MPS = False

    if torch.cuda.is_available() and ALLOW_CUDA:
        DEVICE = "cuda"
    elif (torch.has_mps or torch.backends.mps.is_available()) and ALLOW_MPS:
        DEVICE = "mps"
    print(f"Using device: {DEVICE}")

    tokenizer = CLIPTokenizer("C:/Users/babym/Documents/Coding/Stable_Diffusion_scratch_AI_project/data/tokenizer_vocab.json", merges_file="C:/Users/babym/Documents/Coding/Stable_Diffusion_scratch_AI_project/data/tokenizer_merges.txt")
    models = model_loader.preload_models_from_standard_weights(weights_path, DEVICE)

    ## TEXT TO IMAGE

    #prompt = "A cat stretching on the floor, highly detailed, ultra sharp, cinematic, 100mm lens, 8k resolution." "A dog with sunglasses, wearing comfy hat, looking at camera, highly detailed, ultra sharp, cinematic, 100mm lens, 8k resolution."
    prompt = prompt
    uncond_prompt = ""  # Also known as negative prompt
    do_cfg = True
    cfg_scale = 8  # min: 1, max: 14

    ## IMAGE TO IMAGE

    input_image = None
    # Comment to disable image to image
    image_path = "../images/dog.jpg"
    # input_image = Image.open(image_path)
    # Higher values means more noise will be added to the input image, so the result will further from the input image.
    # Lower values means less noise is added to the input image, so output will be closer to the input image.
    strength = 0.9

    ## SAMPLER

    sampler = "ddpm"
    num_inference_steps = 50
    seed = 42

    output_image = pipeline.generate(
        prompt=prompt,
        uncond_prompt=uncond_prompt,
        input_image=input_image,
        strength=strength,
        do_cfg=do_cfg,
        cfg_scale=cfg_scale,
        sampler_name=sampler,
        n_inference_steps=num_inference_steps,
        seed=seed,
        models=models,
        device=DEVICE,
        idle_device="cpu",
        tokenizer=tokenizer,
    )
    
    return output_image

def image_to_text(image):
    processor = BlipProcessor.from_pretrained("Salesforce/blip-image-captioning-base")
    model = BlipForConditionalGeneration.from_pretrained("Salesforce/blip-image-captioning-base").to("cuda")

    raw_image = Image.open(image).convert('RGB')

    inputs = processor(raw_image, return_tensors="pt").to("cuda")

    out = model.generate(**inputs)
    output = processor.decode(out[0], skip_special_tokens=True)
    return output

def image_to_image(image):
    pipeline = AutoPipelineForImage2Image.from_pretrained("kandinsky-community/kandinsky-2-2-decoder", torch_dtype=torch.float16, use_safetensors=True).to("cuda")
    pipeline.enable_model_cpu_offload()
    pipeline.enable_attention_slicing()

    # prompt = "Futuristic cyberpunk style, neon lights, high-tech details, glowing elements, sleek and edgy design, advanced technology, dystopian yet vibrant atmosphere."
    prompt = "Futuristic cyberpunk style, sleek and edgy design."
    new_image = pipeline(prompt, image=image).images[0]
    print(type(new_image))
    return new_image

# if __name__ == "__main__":
#     # def image_to_image(image):
#     caption = image_to_text("C:/Users/babym/Documents/Coding/Stable_Diffusion_scratch_AI_project/images/wierd_me.jpg")
#     print("Generated Caption:", caption)
